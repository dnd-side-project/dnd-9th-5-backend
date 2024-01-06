package com.dndoz.PosePicker.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;

import com.dndoz.PosePicker.Domain.PoseInfo;
import com.dndoz.PosePicker.Domain.QPoseInfo;
import com.dndoz.PosePicker.Domain.QPoseTag;
import com.dndoz.PosePicker.Domain.QPoseTagAttribute;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class PoseFilterRepositoryImpl implements PoseFilterRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	public PoseFilterRepositoryImpl(JPAQueryFactory queryFactory) {
		this.queryFactory = queryFactory;
	}

	@Override
	public Optional<PoseInfo> findByPoseId(Long pose_id) {
		QPoseInfo qPoseInfo = QPoseInfo.poseInfo;
		QPoseTag qPoseTag = QPoseTag.poseTag;
		QPoseTagAttribute qPoseTagAttribute = QPoseTagAttribute.poseTagAttribute;

		PoseInfo pi = queryFactory
			.selectFrom(qPoseInfo)
			.leftJoin(qPoseTag).on(qPoseInfo.poseId.eq(qPoseTag.poseInfo.poseId))
			.leftJoin(qPoseTagAttribute).on(qPoseTag.poseTagAttribute.attributeId.eq(qPoseTagAttribute.attributeId))
			.where(qPoseInfo.poseId.eq(pose_id))
			.groupBy(qPoseInfo.poseId)
			.fetchOne();	//poseId 고유한 결과-> fetchOne

		List<String> attributes = queryFactory.select(qPoseTagAttribute.attribute)
			.from(qPoseTag)
			.join(qPoseTagAttribute)
			.on(qPoseTag.poseTagAttribute.eq(qPoseTagAttribute))
			.where(qPoseTag.poseInfo.eq(pi))
			.orderBy(qPoseTagAttribute.attributeId.asc())
			.fetch();

		String attributesResult = String.join(",", attributes);
		PoseInfo poseInfo = new PoseInfo(pi, attributesResult);

		return Optional.ofNullable(poseInfo);
	}

	@Override
	public Optional<PoseInfo> findRandomPoseInfo(Long people_count) {
		QPoseInfo qPoseInfo = QPoseInfo.poseInfo;
		QPoseTag qPoseTag = QPoseTag.poseTag;
		QPoseTagAttribute qPoseTagAttribute = QPoseTagAttribute.poseTagAttribute;

		PoseInfo randomPoseInfo = queryFactory
			.selectFrom(qPoseInfo)
			.where(
				eqPeopleCount(qPoseInfo, people_count)
			)
			.orderBy(Expressions.numberTemplate(Double.class, "function('rand')").asc())
			.fetchFirst();	//랜덤 첫번째 결과-> fetchFirst

		List<String> attributes = queryFactory.select(qPoseTagAttribute.attribute)
			.from(qPoseTag)
			.join(qPoseTagAttribute)
			.on(qPoseTag.poseTagAttribute.eq(qPoseTagAttribute))
			.where(qPoseTag.poseInfo.eq(randomPoseInfo))
			.orderBy(qPoseTagAttribute.attributeId.asc())
			.fetch();

		String attributesResult = String.join(",", attributes);
		PoseInfo poseInfo = new PoseInfo(randomPoseInfo, attributesResult);

		return Optional.ofNullable(poseInfo);
	}

	@Override
	public Boolean getRecommendationCheck(Long people_count, Long frame_count, String tags) {
		QPoseInfo qPoseInfo = QPoseInfo.poseInfo;
		QPoseTag qPoseTag = QPoseTag.poseTag;
		QPoseTagAttribute qPoseTagAttribute = QPoseTagAttribute.poseTagAttribute;

		List<PoseInfo> poseInfoList = queryFactory
			.selectFrom(qPoseInfo)
			.where(
				eqPeopleCount(qPoseInfo, people_count),
				eqFrameCount(qPoseInfo, frame_count)
			)
			.orderBy(Expressions.numberTemplate(Double.class, "function('rand')").asc())
			.fetch();

		if (null==tags) {
			System.out.println("PoseFilterRepository: getRecommendationCheck Func : tags=null");
			return false;
		}else{
			List<String> tagsCondition = Arrays.asList(tags.split(","));
			List<PoseInfo> result = new ArrayList<>();
			for (PoseInfo pi : poseInfoList) {
				List<String> attributes = queryFactory.select(qPoseTagAttribute.attribute)
					.from(qPoseTag)
					.join(qPoseTagAttribute)
					.on(qPoseTag.poseTagAttribute.eq(qPoseTagAttribute))
					.where(qPoseTag.poseInfo.eq(pi))
					.orderBy(qPoseTagAttribute.attributeId.asc())
					.fetch();

				if (attributes.containsAll(tagsCondition)) {
					String attributesResult = String.join(",", attributes);

					PoseInfo poseInfo = new PoseInfo(pi, attributesResult);
					result.add(poseInfo);
				}
				if (result.size() > 4)
					return false;
			}
			return true;
		}

	}

	@Override
	public List<PoseInfo> findByFilter(Pageable pageable, Long people_count, Long frame_count, String tags) {
		QPoseInfo qPoseInfo = QPoseInfo.poseInfo;
		QPoseTag qPoseTag = QPoseTag.poseTag;
		QPoseTagAttribute qPoseTagAttribute = QPoseTagAttribute.poseTagAttribute;

		List<PoseInfo> poseInfoList = queryFactory
			.selectFrom(qPoseInfo)
			.where(
				eqPeopleCount(qPoseInfo, people_count),
				eqFrameCount(qPoseInfo, frame_count)
			)
			.orderBy(new OrderSpecifier<>(Order.ASC, Expressions.numberTemplate(Double.class, "rand()")))
			.fetch();

		List<String> tagsCondition = Arrays.asList(tags.split(","));

		List<PoseInfo> result = new ArrayList<>();
		for (PoseInfo pi : poseInfoList) {
			List<String> attributes = queryFactory.select(qPoseTagAttribute.attribute)
				.from(qPoseTag)
				.join(qPoseTagAttribute)
				.on(qPoseTag.poseTagAttribute.eq(qPoseTagAttribute))
				.where(qPoseTag.poseInfo.eq(pi))
				.orderBy(qPoseTagAttribute.attributeId.asc())
				.fetch();

			if (tags.isEmpty() || attributes.containsAll(tagsCondition)) {
				String attributesResult = String.join(",", attributes);

				PoseInfo poseInfo = new PoseInfo(pi, attributesResult);
				result.add(poseInfo);
			}

		}

		return result;
	}

	@Override
	public List<PoseInfo> findByFilterNoTag(Pageable pageable, Long people_count, Long frame_count) {
		QPoseInfo qPoseInfo = QPoseInfo.poseInfo;
		QPoseTag qPoseTag = QPoseTag.poseTag;
		QPoseTagAttribute qPoseTagAttribute = QPoseTagAttribute.poseTagAttribute;

		List<PoseInfo> poseInfoList = queryFactory
			.selectFrom(qPoseInfo)
			.groupBy(qPoseInfo.poseId)	//추가
			.where(
				eqPeopleCount(qPoseInfo, people_count),
				eqFrameCount(qPoseInfo, frame_count)
			)
			.orderBy(new OrderSpecifier<>(Order.ASC, Expressions.numberTemplate(Double.class, "rand()")))
			.fetch();

		List<PoseInfo> result = new ArrayList<>();
		for (PoseInfo pi : poseInfoList) {
			List<String> attributes = queryFactory.select(qPoseTagAttribute.attribute)
				.from(qPoseTag)
				.join(qPoseTagAttribute)
				.on(qPoseTag.poseTagAttribute.eq(qPoseTagAttribute))
				.where(qPoseTag.poseInfo.eq(pi))
				.orderBy(qPoseTagAttribute.attributeId.asc())
				.fetch();

			String attributesResult = String.join(",", attributes);
			PoseInfo poseInfo = new PoseInfo(pi, attributesResult);
			result.add(poseInfo);
		}

		return result;
	}

	@Override
	public List<PoseInfo> getRecommendedContents(Pageable pageable) {
		QPoseInfo qPoseInfo = QPoseInfo.poseInfo;
		QPoseTag qPoseTag = QPoseTag.poseTag;
		QPoseTagAttribute qPoseTagAttribute = QPoseTagAttribute.poseTagAttribute;

		List<PoseInfo> recommendedContents = queryFactory
			.selectFrom(qPoseInfo)
			.groupBy(qPoseInfo.poseId)
			.orderBy(new OrderSpecifier<>(Order.ASC, Expressions.numberTemplate(Double.class, "rand()")))
			.fetch();

		List<PoseInfo> result = new ArrayList<>();
		for (PoseInfo pi : recommendedContents) {
			List<String> attributes = queryFactory.select(qPoseTagAttribute.attribute)
				.from(qPoseTag)
				.join(qPoseTagAttribute)
				.on(qPoseTag.poseTagAttribute.eq(qPoseTagAttribute))
				.where(qPoseTag.poseInfo.eq(pi))
				.orderBy(qPoseTagAttribute.attributeId.asc())
				.fetch();

			String attributesResult = String.join(",", attributes);
			PoseInfo poseInfo = new PoseInfo(pi, attributesResult);
			result.add(poseInfo);
		}

		return result;
	}

	private BooleanExpression eqPeopleCount(QPoseInfo qPoseInfo, Long people_count) {
		if (people_count == null || people_count == 0) {
			return null;
		}
		return people_count < 5 ? qPoseInfo.peopleCount.eq(people_count) : qPoseInfo.peopleCount.goe(5);
	}

	private BooleanExpression eqFrameCount(QPoseInfo qPoseInfo, Long frame_count) {
		if (frame_count == null || frame_count == 0) {
			return null;
		}
		return qPoseInfo.frameCount.eq(frame_count);
	}

	private BooleanExpression containsTagAttribute(QPoseTagAttribute qPoseTagAttribute, String tags) {
		if (tags == null || tags.isEmpty()) {
			return null;
		}
		return qPoseTagAttribute.attribute.in(tags.split(","));
	}
}



package pl.lublin.zeto.hermesJpaHelper;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

public class JpaSpecification<T> implements Specification<T>{
	
	private final SearchCriteria criteria;
	private final Class<?> entityClass;
	private final Class<?> entityStructureClass;
	
	public JpaSpecification(SearchCriteria criteria, Class<?> entityClass, Class<?> entityStructureClass) {
		this.criteria = criteria;
		this.entityClass = entityClass;
		this.entityStructureClass = entityStructureClass;
	}
	
	@Override
	public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
		
		switch (criteria.getOperator()) {
			case EQUAL: 
				return cb.equal(getExp(root, criteria.getKey()), criteria.getValue());
				
			case NOT_EQUAL: 
				return cb.notEqual(getExp(root, criteria.getKey()), criteria.getValue());
				
			case EQUAL_IGNORE_CASE: 
				return cb.equal(cb.lower(getExp(root, criteria.getKey())), criteria.getValue().toString().toLowerCase());
				
			case LIKE: 
				return cb.like(cb.lower(getExp(root, criteria.getKey())), "%" + criteria.getValue().toString().toLowerCase() + "%");
				
			case GREATER_THAN: 
				return cb.greaterThan(getExp(root, criteria.getKey()), criteria.getValue().toString());
				
			case GREATER_THAN_OR_EQUAL: 
				return cb.greaterThanOrEqualTo(getExp(root, criteria.getKey()), criteria.getValue().toString());
				
			case LESS_THAN: 
				return cb.lessThan(getExp(root, criteria.getKey()), criteria.getValue().toString());
				
			case LESS_THAN_OR_EQUAL: 
				return cb.lessThanOrEqualTo(getExp(root, criteria.getKey()), criteria.getValue().toString());
				
			case IS_NULL: 
				return cb.isNull(getExp(root, criteria.getKey()));
				
			case NOT_NULL: 
				return cb.isNotNull(getExp(root, criteria.getKey()));
			
			case BETWEEN: 
				String[] values = criteria.getValue().toString().split("bt");
				return cb.between(getExp(root, criteria.getKey()), values[0], values[1]);
			
			case IN_ARTICLE_STRUCTURE:

				return ArticleStructurePredicate.get(
						root, 
						query, 
						cb, 
						getExp(root, criteria.getKey()), 
						ArticleStructurePredicate.Model.build(criteria.getValue().toString().split("level")),
						entityClass,
						entityStructureClass
						);
				
			default:
				return null;
		}

	}
	
	private Path<String> getExp(Path<?> path, String key) {
		LinkedList<String> keys = new LinkedList<>(Arrays.asList(key.split("\\.")));

		if (keys.size() > 1) {	
			Class<?> type = path.get(keys.peek()).getJavaType();
			
			if (Collection.class.isAssignableFrom(type)) {	
				Optional<Join<?,?>> join = getJoin(path, keys.peek());
				if (join.isPresent()) {			
					return getExp(join.get(), getKeyAfterPoll(keys));
				} else {
					return getExp(((Root<?>) path).join(keys.poll()), keys.stream().collect(Collectors.joining(".")));
				}
			}			
			return getExp(path.get(keys.poll()), keys.stream().collect(Collectors.joining(".")));
		}
		
		return path.get(keys.poll());
	}

	@SuppressWarnings("unchecked")
	private Optional<Join<?, ?>> getJoin(Path<?> path, String joinName) {
		Optional<?> findFirst = ((Root<?>) path).getJoins()
			.stream()
			.filter(e -> e.getAttribute().getName().equals(joinName))
			.findFirst();
		return (Optional<Join<?, ?>>) findFirst;
	}
	
	private String getKeyAfterPoll(LinkedList<String> keys) {
		keys.poll();
		return keys.stream().collect(Collectors.joining("."));
	}
}

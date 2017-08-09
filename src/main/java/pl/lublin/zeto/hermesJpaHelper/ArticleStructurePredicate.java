package pl.lublin.zeto.hermesJpaHelper;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

/**
 * Class is used to create predicates for search articles whose belong to a given structure level
 * 
 * @author michalbabula
 *
 */
public class ArticleStructurePredicate {
	
	/**
	 * 
	 * @param root {@code Root<?>}
	 * @param query {@code CriteriaQuery<?>}
	 * @param cb {@code CriteriaBuilder<?>}
	 * @param path {@code Expression<?>} path to article.articleStructure.id
	 * @param model (structure id, structure level)
	 * @param entity (@code Object)
	 * @param entityStructure (@code Object)
	 * @return Predicate
	 */
	public static Predicate get(
			Root<?> root, 
			CriteriaQuery<?> query, 
			CriteriaBuilder cb, 
			Path<?> path,
			Model model,
			Class<?> entityClass,
			Class<?> entityStructureClass) 
	{	
		Predicate predicate = null;
		Subquery<?> sq = query.subquery(entityClass);
		Root<?> str = sq.from(entityStructureClass);
		
		switch(model.getLevel()) {
			case 0:
				predicate = path.in(
						sq.select(str.get("id")).where(
								cb.equal(parentGetter(str, 5).get("id"), model.getId())));
				break;
			case 1:
				predicate = path.in(
						sq.select(str.get("id")).where(
								cb.equal(parentGetter(str, 4).get("id"), model.getId())));
				break;
			case 2:
				predicate = path.in(
						sq.select(str.get("id")).where(
								cb.equal(parentGetter(str, 3).get("id"), model.getId())));
				break;
			case 3:
				predicate = path.in(
						sq.select(str.get("id")).where(
								cb.equal(parentGetter(str, 2).get("id"), model.getId())));
				break;
			case 4:
				predicate = path.in(
						sq.select(str.get("id")).where(
								cb.equal(parentGetter(str, 1).get("id"), model.getId())));
				break;
			case 5:
				predicate = path.in(
						sq.select(str.get("id")).where(
								cb.equal(parentGetter(str, 0).get("id"), model.getId())));
				break;
			case 6:
				predicate = cb.equal(path, model.getId());
				break;
		}
		
		return predicate;
	}
	
	private static Path<?> parentGetter(Path<?> exp, int iteration) {
		if (iteration > 0) {
			return parentGetter(exp.get("parent"), --iteration);
		}
		
		return exp.get("parent");
	}
	
	public static class Model {
		private int id;
		private int level;
		
		private Model(String[] values) {
			this.id = Integer.parseInt(values[0]);
			this.level = Integer.parseInt(values[1]);
		}
		
		public static Model build(String[] values) {
			return new Model(values);
		}
		
		public int getId() {
			return id;
		}
		
		public int getLevel() {
			return level;
		}
	}
}

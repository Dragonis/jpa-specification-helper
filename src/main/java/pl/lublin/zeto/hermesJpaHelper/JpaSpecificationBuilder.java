package pl.lublin.zeto.hermesJpaHelper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

public class JpaSpecificationBuilder<T> {
	
	public enum ConjunctiveOperator { AND, OR }
	
	private final ConjunctiveOperator operator;
	private final List<SearchCriteria> params;
	private Class<?> entity;
	private Class<?> entityStructure;
	 
    public JpaSpecificationBuilder() {
        this.params = new ArrayList<SearchCriteria>();
        this.operator = ConjunctiveOperator.AND;
    }
    
    public JpaSpecificationBuilder(ConjunctiveOperator operator) {
    	this.params = new ArrayList<SearchCriteria>();
    	this.operator = operator;
    }
    
    public JpaSpecificationBuilder<T> with(String key, String operation, Object value) {
        params.add(new SearchCriteria(key, operation, value));
        return this;
    }
 
    public Specification<T> build() {
        if (params.size() == 0) {
            return null;
        }
        
        List<JpaSpecification<T>> specs = new ArrayList<JpaSpecification<T>>();
        params.forEach(e -> specs.add(new JpaSpecification<T>(e,entity,entityStructure)));
 
        Specification<T> result = specs.get(0);
        
        if (operator == ConjunctiveOperator.AND) {
        	for (int i = 1; i < specs.size(); i++)
            	result = Specifications.where(result).and(specs.get(i));
        }
        else if (operator == ConjunctiveOperator.OR) {
        	for (int i = 1; i < specs.size(); i++)
            	result = Specifications.where(result).or(specs.get(i));
        }
        
        return result;
    }
}

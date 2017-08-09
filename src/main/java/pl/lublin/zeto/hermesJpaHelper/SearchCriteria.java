package pl.lublin.zeto.hermesJpaHelper;

public class SearchCriteria {
	
	private String key;
	private CriteriaOperator operator;
	private Object value;
	
	public SearchCriteria(String key, CriteriaOperator operator, Object value) {
		this.key = key;
		this.operator = operator;
		this.value = value;
	}
	
	public SearchCriteria(String key, String operator, Object value) {
		this.key = key;
		this.operator = CriteriaOperator.getByOperator(operator);
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public CriteriaOperator getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = CriteriaOperator.getByOperator(operator);
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "SearchCriteria [key=" + key + ", operator=" + operator + ", value=" + value + "]";
	}
	
}

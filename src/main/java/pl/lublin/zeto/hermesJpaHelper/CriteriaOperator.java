package pl.lublin.zeto.hermesJpaHelper;

import java.util.Arrays;

public enum CriteriaOperator {
	
	EQUAL ("=="),
	NOT_EQUAL("!="),
	EQUAL_IGNORE_CASE("=ic="),
	LIKE(":"),
	GREATER_THAN(">>"),
	GREATER_THAN_OR_EQUAL(">="),
	LESS_THAN("<<"),
	LESS_THAN_OR_EQUAL("<="),
	IS_NULL("=null="),
	NOT_NULL("!=null="),
	BETWEEN("=bt="),
	IN_ARTICLE_STRUCTURE("=str=");
	
	final String operator;
	
	private CriteriaOperator(String operator) {
		this.operator = operator;
	}
	
	public static CriteriaOperator getByOperator(String operator) {
		return Arrays.asList(CriteriaOperator.values()).stream()
			.filter(e -> e.operator.equals(operator))
			.findAny()
			.orElseThrow(() -> new IllegalArgumentException("There is no CriteriaOperator enum with operator: " + operator));
	}
	
}

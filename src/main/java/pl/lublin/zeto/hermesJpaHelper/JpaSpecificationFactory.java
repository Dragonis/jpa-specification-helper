package pl.lublin.zeto.hermesJpaHelper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;

import pl.lublin.zeto.hermesJpaHelper.JpaSpecificationBuilder.ConjunctiveOperator;

public class JpaSpecificationFactory<T> {
	
	private final Logger LOG = LoggerFactory.getLogger(this.getClass());
	
	private final Pattern pattern = Pattern.compile("([\\w.]+?)(:|<<|>>|==|!=|=ic=|>=|<=|=null=|!=null=|=bt=|=str=)([^,]{1,}),");
	private final JpaSpecificationBuilder<T> builder;
	
	public JpaSpecificationFactory() {
		this.builder = new JpaSpecificationBuilder<>();
	}
	
	public JpaSpecificationFactory(ConjunctiveOperator operator) {
		this.builder = new JpaSpecificationBuilder<>(operator);
	}
	
	public Specification<T> get(String search) {
		Matcher matcher = pattern.matcher(search + ",");
		
		while (matcher.find()) {
			LOG.debug("Search criteria key: [{}], operator: [{}], value: [{}]", matcher.group(1), matcher.group(2), matcher.group(3));
			builder.with(matcher.group(1), matcher.group(2), matcher.group(3));
		}
		
		return builder.build();
	}
}

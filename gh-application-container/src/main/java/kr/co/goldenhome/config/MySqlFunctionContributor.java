package kr.co.goldenhome.config;

import org.hibernate.boot.model.FunctionContributions;
import org.hibernate.boot.model.FunctionContributor;
import org.hibernate.type.StandardBasicTypes;

public class MySqlFunctionContributor implements FunctionContributor {
    @Override
    public void contributeFunctions(FunctionContributions functionContributions) {
        var registry = functionContributions.getFunctionRegistry();
        var doubleType = functionContributions.getTypeConfiguration().getBasicTypeRegistry().resolve(StandardBasicTypes.DOUBLE);

        registry.registerPattern("match_single", "MATCH(?1) AGAINST(?2 IN NATURAL LANGUAGE MODE)", doubleType);
        registry.registerPattern("match_double", "MATCH(?1, ?2) AGAINST(?3 IN NATURAL LANGUAGE MODE)", doubleType);
    }
}

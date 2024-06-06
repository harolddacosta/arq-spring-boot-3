/* AssentSoftware (C)2023 */
package com.decathlon.logging.audit.annotation;

import static org.assertj.core.api.Assertions.assertThat;

import com.decathlon.logging.audit.AuditableNestedParamBean;
import com.decathlon.logging.audit.AuditableParamBean;
import com.decathlon.logging.audit.annotation.Auditable.ActionResultEnum;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

class AuditBuilderInfoTest {

    @Test
    void test_KeyValueResponseDto_BeanVerifier() {
        AuditableParamBean auditableParamBean = new AuditableParamBean();
        auditableParamBean.setAddress(new AuditableNestedParamBean("Cornella", "08940"));

        Map<String, String> params = new HashMap<>();
        params.put("CodigoPostalCharallave", auditableParamBean.getAddress().getPostalCode());

        AuditBuilderInfo bean =
                AuditBuilderInfo.builder()
                        .params(params)
                        .description("This description was generated from the builder")
                        .eventLogCategory("Category set from the builder")
                        .eventLogCode("Log set from the builder")
                        .result(ActionResultEnum.OK)
                        .build();

        assertThat(bean.getParams()).isNotNull();
        assertThat(bean.getDescription())
                .isEqualTo("This description was generated from the builder");
        assertThat(bean.getEventLogCategory()).isEqualTo("Category set from the builder");
    }
}

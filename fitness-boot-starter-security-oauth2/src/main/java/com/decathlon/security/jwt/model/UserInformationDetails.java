/* Decathlon (C)2023 */
package com.decathlon.security.jwt.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInformationDetails implements Serializable {

    private static final long serialVersionUID = -8610496656497346777L;

    private UUID entryUUID;
    private String sub;
    private String preferredLanguage;
    private List<String> role = Collections.emptyList();
    private String c;
    private String mail;
    private String sitepartynumber;
    private String displayName;
    private String givenName;
    private String cn;
    private String sitetype;
    private String title;

    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<String> objectclass = Collections.emptyList();

    private UUID uuid;
    private String uid;
    private String site;
    private String federationIdp;
    private String familyName;
    private String sitename;
    private String sn;

    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<String> partyNumber = Collections.emptyList();

    private String costcenter;
    private String jobname;
}

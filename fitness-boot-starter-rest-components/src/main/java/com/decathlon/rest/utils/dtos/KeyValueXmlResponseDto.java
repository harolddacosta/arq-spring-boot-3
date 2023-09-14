/* Decathlon (C)2023 */
package com.decathlon.rest.utils.dtos;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@XmlRootElement(name = "keyValueXmlResponseDto")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class KeyValueXmlResponseDto implements Serializable {

    private static final long serialVersionUID = -942697419901293364L;

    @XmlElement private String value;
}

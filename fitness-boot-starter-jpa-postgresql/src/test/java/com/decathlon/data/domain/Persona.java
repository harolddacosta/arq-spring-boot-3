/* Decathlon (C)2023 */
package com.decathlon.data.domain;
// Generated 10 jul. 2021 0:09:56 by Hibernate Tools 3.2.2.GA and Assent Architecture

import com.decathlon.core.domain.types.Auditable;
import com.decathlon.core.domain.types.Identifiable;
import com.decathlon.core.domain.types.Versionable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

/** Persona generated by hbm2java */
@Entity
@EntityListeners({AuditingEntityListener.class})
@Table(
        name = "persona",
        schema = "public",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"cedula_persona"}),
            @UniqueConstraint(columnNames = {"codigo_persona"}),
            @UniqueConstraint(columnNames = {"numero_msas"})
        })
public class Persona implements Versionable, Identifiable, Auditable, java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private long version;

    private String codigoPersona;

    private String cedulaPersona;

    private String nombrePersona;

    private String apellidoPersona;

    private String rifPersona;

    private String numeroMsas;

    private LocalDate fechaNacimientoPersona;

    private LocalDate fechaDefuncionPersona;

    private LocalDate fechaVencimientoRif;

    private String telefonoMovilPersona;

    private String telefonoFijoPersona;

    private String EMailPersona;

    private String direccionPersona;

    private String nombreContactoUno;

    private String telefonoContactoUno;

    private String telefonoMovilContactoUno;

    private String emailContactoUno;

    private String parentescoContactoUno;

    private String nombreContactoDos;

    private String telefonoContactoDos;

    private String telefonoMovilContactoDos;

    private String emailContactoDos;

    private String parentescoContactoDos;

    private Float edad;

    private String url1;

    private String url1FileName;

    private Double url1FileSize;

    private String comentariosAdicionales;

    private String nombreDoctorReferencia;

    private String telefonoContactoDoctorReferencia;

    private String telefonoMovilDoctorReferencia;

    private String emailDoctorReferencia;

    private String lugarNacimientoOtro;

    private String direccionPersonaCalleAvenida;

    private String direccionPersonaEdificio;

    private String direccionPersonaConsultorio;
    @CreatedBy private String usuarioCreacion;
    @CreatedDate private LocalDateTime fechaCreacion;
    @LastModifiedBy private String usuarioModificacion;
    @LastModifiedDate private LocalDateTime fechaModificacion;

    public Persona() {
        // default constructor
    }

    public Persona(
            String codigoPersona,
            String cedulaPersona,
            String nombrePersona,
            String telefonoFijoPersona,
            String direccionPersona) {
        this.codigoPersona = codigoPersona;
        this.cedulaPersona = cedulaPersona;
        this.nombrePersona = nombrePersona;
        this.telefonoFijoPersona = telefonoFijoPersona;
        this.direccionPersona = direccionPersona;
    }

    public Persona(
            String codigoPersona,
            String cedulaPersona,
            String nombrePersona,
            String apellidoPersona,
            String rifPersona,
            String numeroMsas,
            LocalDate fechaNacimientoPersona,
            LocalDate fechaDefuncionPersona,
            LocalDate fechaVencimientoRif,
            String telefonoMovilPersona,
            String telefonoFijoPersona,
            String EMailPersona,
            String direccionPersona,
            String nombreContactoUno,
            String telefonoContactoUno,
            String telefonoMovilContactoUno,
            String emailContactoUno,
            String parentescoContactoUno,
            String nombreContactoDos,
            String telefonoContactoDos,
            String telefonoMovilContactoDos,
            String emailContactoDos,
            String parentescoContactoDos,
            Float edad,
            String url1,
            String url1FileName,
            Double url1FileSize,
            String comentariosAdicionales,
            String nombreDoctorReferencia,
            String telefonoContactoDoctorReferencia,
            String telefonoMovilDoctorReferencia,
            String emailDoctorReferencia,
            String lugarNacimientoOtro,
            String direccionPersonaCalleAvenida,
            String direccionPersonaEdificio,
            String direccionPersonaConsultorio,
            String usuarioCreacion,
            LocalDateTime fechaCreacion,
            String usuarioModificacion,
            LocalDateTime fechaModificacion) {
        this.codigoPersona = codigoPersona;
        this.cedulaPersona = cedulaPersona;
        this.nombrePersona = nombrePersona;
        this.apellidoPersona = apellidoPersona;
        this.rifPersona = rifPersona;
        this.numeroMsas = numeroMsas;
        this.fechaNacimientoPersona = fechaNacimientoPersona;
        this.fechaDefuncionPersona = fechaDefuncionPersona;
        this.fechaVencimientoRif = fechaVencimientoRif;
        this.telefonoMovilPersona = telefonoMovilPersona;
        this.telefonoFijoPersona = telefonoFijoPersona;
        this.EMailPersona = EMailPersona;
        this.direccionPersona = direccionPersona;
        this.nombreContactoUno = nombreContactoUno;
        this.telefonoContactoUno = telefonoContactoUno;
        this.telefonoMovilContactoUno = telefonoMovilContactoUno;
        this.emailContactoUno = emailContactoUno;
        this.parentescoContactoUno = parentescoContactoUno;
        this.nombreContactoDos = nombreContactoDos;
        this.telefonoContactoDos = telefonoContactoDos;
        this.telefonoMovilContactoDos = telefonoMovilContactoDos;
        this.emailContactoDos = emailContactoDos;
        this.parentescoContactoDos = parentescoContactoDos;
        this.edad = edad;
        this.url1 = url1;
        this.url1FileName = url1FileName;
        this.url1FileSize = url1FileSize;
        this.comentariosAdicionales = comentariosAdicionales;
        this.nombreDoctorReferencia = nombreDoctorReferencia;
        this.telefonoContactoDoctorReferencia = telefonoContactoDoctorReferencia;
        this.telefonoMovilDoctorReferencia = telefonoMovilDoctorReferencia;
        this.emailDoctorReferencia = emailDoctorReferencia;
        this.lugarNacimientoOtro = lugarNacimientoOtro;
        this.direccionPersonaCalleAvenida = direccionPersonaCalleAvenida;
        this.direccionPersonaEdificio = direccionPersonaEdificio;
        this.direccionPersonaConsultorio = direccionPersonaConsultorio;
        this.usuarioCreacion = usuarioCreacion;
        this.fechaCreacion = fechaCreacion;
        this.usuarioModificacion = usuarioModificacion;
        this.fechaModificacion = fechaModificacion;
    }

    @GenericGenerator(
            name = "persona_seq_gen",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                @Parameter(name = "sequence_name", value = "persona_id_seq"),
                @Parameter(name = "initial_value", value = "1"),
                @Parameter(name = "increment_size", value = "1")
            })
    @GeneratedValue(generator = "persona_seq_gen")
    @Id
    @Column(name = "id", unique = true, nullable = false)
    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Version
    @Column(name = "version")
    public long getVersion() {
        return this.version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    @Column(name = "codigo_persona", nullable = false, length = 16)
    public String getCodigoPersona() {
        return this.codigoPersona;
    }

    public void setCodigoPersona(String codigoPersona) {
        this.codigoPersona = codigoPersona;
    }

    @Column(name = "cedula_persona", nullable = false, length = 64)
    public String getCedulaPersona() {
        return this.cedulaPersona;
    }

    public void setCedulaPersona(String cedulaPersona) {
        this.cedulaPersona = cedulaPersona;
    }

    @Column(name = "nombre_persona", nullable = false, length = 64)
    public String getNombrePersona() {
        return this.nombrePersona;
    }

    public void setNombrePersona(String nombrePersona) {
        this.nombrePersona = nombrePersona;
    }

    @Column(name = "apellido_persona", length = 64)
    public String getApellidoPersona() {
        return this.apellidoPersona;
    }

    public void setApellidoPersona(String apellidoPersona) {
        this.apellidoPersona = apellidoPersona;
    }

    @Column(name = "rif_persona", length = 64)
    public String getRifPersona() {
        return this.rifPersona;
    }

    public void setRifPersona(String rifPersona) {
        this.rifPersona = rifPersona;
    }

    @Column(name = "numero_msas")
    public String getNumeroMsas() {
        return this.numeroMsas;
    }

    public void setNumeroMsas(String numeroMsas) {
        this.numeroMsas = numeroMsas;
    }

    @Column(name = "fecha_nacimiento_persona", length = 13)
    public LocalDate getFechaNacimientoPersona() {
        return this.fechaNacimientoPersona;
    }

    public void setFechaNacimientoPersona(LocalDate fechaNacimientoPersona) {
        this.fechaNacimientoPersona = fechaNacimientoPersona;
    }

    @Column(name = "fecha_defuncion_persona", length = 13)
    public LocalDate getFechaDefuncionPersona() {
        return this.fechaDefuncionPersona;
    }

    public void setFechaDefuncionPersona(LocalDate fechaDefuncionPersona) {
        this.fechaDefuncionPersona = fechaDefuncionPersona;
    }

    @Column(name = "fecha_vencimiento_rif", length = 13)
    public LocalDate getFechaVencimientoRif() {
        return this.fechaVencimientoRif;
    }

    public void setFechaVencimientoRif(LocalDate fechaVencimientoRif) {
        this.fechaVencimientoRif = fechaVencimientoRif;
    }

    @Column(name = "telefono_movil_persona", length = 32)
    public String getTelefonoMovilPersona() {
        return this.telefonoMovilPersona;
    }

    public void setTelefonoMovilPersona(String telefonoMovilPersona) {
        this.telefonoMovilPersona = telefonoMovilPersona;
    }

    @Column(name = "telefono_fijo_persona", nullable = false, length = 32)
    public String getTelefonoFijoPersona() {
        return this.telefonoFijoPersona;
    }

    public void setTelefonoFijoPersona(String telefonoFijoPersona) {
        this.telefonoFijoPersona = telefonoFijoPersona;
    }

    @Column(name = "e_mail_persona", length = 64)
    public String getEMailPersona() {
        return this.EMailPersona;
    }

    public void setEMailPersona(String EMailPersona) {
        this.EMailPersona = EMailPersona;
    }

    @Column(name = "direccion_persona", nullable = false)
    public String getDireccionPersona() {
        return this.direccionPersona;
    }

    public void setDireccionPersona(String direccionPersona) {
        this.direccionPersona = direccionPersona;
    }

    @Column(name = "nombre_contacto_uno", length = 64)
    public String getNombreContactoUno() {
        return this.nombreContactoUno;
    }

    public void setNombreContactoUno(String nombreContactoUno) {
        this.nombreContactoUno = nombreContactoUno;
    }

    @Column(name = "telefono_contacto_uno", length = 32)
    public String getTelefonoContactoUno() {
        return this.telefonoContactoUno;
    }

    public void setTelefonoContactoUno(String telefonoContactoUno) {
        this.telefonoContactoUno = telefonoContactoUno;
    }

    @Column(name = "telefono_movil_contacto_uno", length = 32)
    public String getTelefonoMovilContactoUno() {
        return this.telefonoMovilContactoUno;
    }

    public void setTelefonoMovilContactoUno(String telefonoMovilContactoUno) {
        this.telefonoMovilContactoUno = telefonoMovilContactoUno;
    }

    @Column(name = "email_contacto_uno", length = 64)
    public String getEmailContactoUno() {
        return this.emailContactoUno;
    }

    public void setEmailContactoUno(String emailContactoUno) {
        this.emailContactoUno = emailContactoUno;
    }

    @Column(name = "parentesco_contacto_uno")
    public String getParentescoContactoUno() {
        return this.parentescoContactoUno;
    }

    public void setParentescoContactoUno(String parentescoContactoUno) {
        this.parentescoContactoUno = parentescoContactoUno;
    }

    @Column(name = "nombre_contacto_dos", length = 64)
    public String getNombreContactoDos() {
        return this.nombreContactoDos;
    }

    public void setNombreContactoDos(String nombreContactoDos) {
        this.nombreContactoDos = nombreContactoDos;
    }

    @Column(name = "telefono_contacto_dos", length = 32)
    public String getTelefonoContactoDos() {
        return this.telefonoContactoDos;
    }

    public void setTelefonoContactoDos(String telefonoContactoDos) {
        this.telefonoContactoDos = telefonoContactoDos;
    }

    @Column(name = "telefono_movil_contacto_dos", length = 32)
    public String getTelefonoMovilContactoDos() {
        return this.telefonoMovilContactoDos;
    }

    public void setTelefonoMovilContactoDos(String telefonoMovilContactoDos) {
        this.telefonoMovilContactoDos = telefonoMovilContactoDos;
    }

    @Column(name = "email_contacto_dos", length = 64)
    public String getEmailContactoDos() {
        return this.emailContactoDos;
    }

    public void setEmailContactoDos(String emailContactoDos) {
        this.emailContactoDos = emailContactoDos;
    }

    @Column(name = "parentesco_contacto_dos")
    public String getParentescoContactoDos() {
        return this.parentescoContactoDos;
    }

    public void setParentescoContactoDos(String parentescoContactoDos) {
        this.parentescoContactoDos = parentescoContactoDos;
    }

    @Column(name = "edad", precision = 8, scale = 8)
    public Float getEdad() {
        return this.edad;
    }

    public void setEdad(Float edad) {
        this.edad = edad;
    }

    @Column(name = "url1")
    public String getUrl1() {
        return this.url1;
    }

    public void setUrl1(String url1) {
        this.url1 = url1;
    }

    @Column(name = "url1_file_name")
    public String getUrl1FileName() {
        return this.url1FileName;
    }

    public void setUrl1FileName(String url1FileName) {
        this.url1FileName = url1FileName;
    }

    @Column(name = "url1_file_size", precision = 17, scale = 17)
    public Double getUrl1FileSize() {
        return this.url1FileSize;
    }

    public void setUrl1FileSize(Double url1FileSize) {
        this.url1FileSize = url1FileSize;
    }

    @Column(name = "comentarios_adicionales")
    public String getComentariosAdicionales() {
        return this.comentariosAdicionales;
    }

    public void setComentariosAdicionales(String comentariosAdicionales) {
        this.comentariosAdicionales = comentariosAdicionales;
    }

    @Column(name = "nombre_doctor_referencia")
    public String getNombreDoctorReferencia() {
        return this.nombreDoctorReferencia;
    }

    public void setNombreDoctorReferencia(String nombreDoctorReferencia) {
        this.nombreDoctorReferencia = nombreDoctorReferencia;
    }

    @Column(name = "telefono_contacto_doctor_referencia", length = 32)
    public String getTelefonoContactoDoctorReferencia() {
        return this.telefonoContactoDoctorReferencia;
    }

    public void setTelefonoContactoDoctorReferencia(String telefonoContactoDoctorReferencia) {
        this.telefonoContactoDoctorReferencia = telefonoContactoDoctorReferencia;
    }

    @Column(name = "telefono_movil_doctor_referencia", length = 32)
    public String getTelefonoMovilDoctorReferencia() {
        return this.telefonoMovilDoctorReferencia;
    }

    public void setTelefonoMovilDoctorReferencia(String telefonoMovilDoctorReferencia) {
        this.telefonoMovilDoctorReferencia = telefonoMovilDoctorReferencia;
    }

    @Column(name = "email_doctor_referencia", length = 64)
    public String getEmailDoctorReferencia() {
        return this.emailDoctorReferencia;
    }

    public void setEmailDoctorReferencia(String emailDoctorReferencia) {
        this.emailDoctorReferencia = emailDoctorReferencia;
    }

    @Column(name = "lugar_nacimiento_otro", length = 128)
    public String getLugarNacimientoOtro() {
        return this.lugarNacimientoOtro;
    }

    public void setLugarNacimientoOtro(String lugarNacimientoOtro) {
        this.lugarNacimientoOtro = lugarNacimientoOtro;
    }

    @Column(name = "direccion_persona_calle_avenida")
    public String getDireccionPersonaCalleAvenida() {
        return this.direccionPersonaCalleAvenida;
    }

    public void setDireccionPersonaCalleAvenida(String direccionPersonaCalleAvenida) {
        this.direccionPersonaCalleAvenida = direccionPersonaCalleAvenida;
    }

    @Column(name = "direccion_persona_edificio")
    public String getDireccionPersonaEdificio() {
        return this.direccionPersonaEdificio;
    }

    public void setDireccionPersonaEdificio(String direccionPersonaEdificio) {
        this.direccionPersonaEdificio = direccionPersonaEdificio;
    }

    @Column(name = "direccion_persona_consultorio")
    public String getDireccionPersonaConsultorio() {
        return this.direccionPersonaConsultorio;
    }

    public void setDireccionPersonaConsultorio(String direccionPersonaConsultorio) {
        this.direccionPersonaConsultorio = direccionPersonaConsultorio;
    }

    @Override
    @Column(name = "usuario_creacion")
    public String getCreatedBy() {
        return usuarioCreacion;
    }

    @Override
    public void setCreatedBy(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    @Override
    @Column(name = "fecha_creacion")
    public LocalDateTime getCreatedAt() {
        return fechaCreacion;
    }

    @Override
    public void setCreatedAt(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    @Override
    @Column(name = "usuario_modificacion")
    public String getLastModifiedBy() {
        return usuarioModificacion;
    }

    @Override
    public void setLastModifiedBy(String usuarioModificacion) {
        this.usuarioModificacion = usuarioModificacion;
    }

    @Override
    @Column(name = "fecha_modificacion")
    public LocalDateTime getLastModifiedDate() {
        return fechaModificacion;
    }

    @Override
    public void setLastModifiedDate(LocalDateTime fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    /**
     * toString
     *
     * @return String
     */
    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();

        buffer.append(getClass().getName())
                .append("@")
                .append(Integer.toHexString(hashCode()))
                .append(" [");
        buffer.append("id").append("='").append(getId()).append("' ");
        buffer.append("version").append("='").append(getVersion()).append("' ");
        buffer.append("codigoPersona").append("='").append(getCodigoPersona()).append("' ");
        buffer.append("cedulaPersona").append("='").append(getCedulaPersona()).append("' ");
        buffer.append("nombrePersona").append("='").append(getNombrePersona()).append("' ");
        buffer.append("apellidoPersona").append("='").append(getApellidoPersona()).append("' ");
        buffer.append("rifPersona").append("='").append(getRifPersona()).append("' ");
        buffer.append("numeroMsas").append("='").append(getNumeroMsas()).append("' ");
        buffer.append("fechaNacimientoPersona")
                .append("='")
                .append(getFechaNacimientoPersona())
                .append("' ");
        buffer.append("fechaDefuncionPersona")
                .append("='")
                .append(getFechaDefuncionPersona())
                .append("' ");
        buffer.append("fechaVencimientoRif")
                .append("='")
                .append(getFechaVencimientoRif())
                .append("' ");
        buffer.append("telefonoMovilPersona")
                .append("='")
                .append(getTelefonoMovilPersona())
                .append("' ");
        buffer.append("telefonoFijoPersona")
                .append("='")
                .append(getTelefonoFijoPersona())
                .append("' ");
        buffer.append("EMailPersona").append("='").append(getEMailPersona()).append("' ");
        buffer.append("direccionPersona").append("='").append(getDireccionPersona()).append("' ");
        buffer.append("nombreContactoUno").append("='").append(getNombreContactoUno()).append("' ");
        buffer.append("telefonoContactoUno")
                .append("='")
                .append(getTelefonoContactoUno())
                .append("' ");
        buffer.append("telefonoMovilContactoUno")
                .append("='")
                .append(getTelefonoMovilContactoUno())
                .append("' ");
        buffer.append("emailContactoUno").append("='").append(getEmailContactoUno()).append("' ");
        buffer.append("parentescoContactoUno")
                .append("='")
                .append(getParentescoContactoUno())
                .append("' ");
        buffer.append("nombreContactoDos").append("='").append(getNombreContactoDos()).append("' ");
        buffer.append("telefonoContactoDos")
                .append("='")
                .append(getTelefonoContactoDos())
                .append("' ");
        buffer.append("telefonoMovilContactoDos")
                .append("='")
                .append(getTelefonoMovilContactoDos())
                .append("' ");
        buffer.append("emailContactoDos").append("='").append(getEmailContactoDos()).append("' ");
        buffer.append("parentescoContactoDos")
                .append("='")
                .append(getParentescoContactoDos())
                .append("' ");
        buffer.append("edad").append("='").append(getEdad()).append("' ");
        buffer.append("url1").append("='").append(getUrl1()).append("' ");
        buffer.append("url1FileName").append("='").append(getUrl1FileName()).append("' ");
        buffer.append("url1FileSize").append("='").append(getUrl1FileSize()).append("' ");
        buffer.append("comentariosAdicionales")
                .append("='")
                .append(getComentariosAdicionales())
                .append("' ");
        buffer.append("nombreDoctorReferencia")
                .append("='")
                .append(getNombreDoctorReferencia())
                .append("' ");
        buffer.append("telefonoContactoDoctorReferencia")
                .append("='")
                .append(getTelefonoContactoDoctorReferencia())
                .append("' ");
        buffer.append("telefonoMovilDoctorReferencia")
                .append("='")
                .append(getTelefonoMovilDoctorReferencia())
                .append("' ");
        buffer.append("emailDoctorReferencia")
                .append("='")
                .append(getEmailDoctorReferencia())
                .append("' ");
        buffer.append("lugarNacimientoOtro")
                .append("='")
                .append(getLugarNacimientoOtro())
                .append("' ");
        buffer.append("direccionPersonaCalleAvenida")
                .append("='")
                .append(getDireccionPersonaCalleAvenida())
                .append("' ");
        buffer.append("direccionPersonaEdificio")
                .append("='")
                .append(getDireccionPersonaEdificio())
                .append("' ");
        buffer.append("direccionPersonaConsultorio")
                .append("='")
                .append(getDireccionPersonaConsultorio())
                .append("' ");
        buffer.append("usuarioCreacion").append("='").append(getCreatedBy()).append("' ");
        buffer.append("fechaCreacion").append("='").append(getCreatedAt()).append("' ");
        buffer.append("usuarioModificacion").append("='").append(getLastModifiedBy()).append("' ");
        buffer.append("fechaModificacion").append("='").append(getLastModifiedDate()).append("' ");
        buffer.append("]");

        return buffer.toString();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;

        if (other == null) return false;

        if (!(other instanceof Persona)) return false;

        Persona castOther = (Persona) other;

        return ((this.getId() == castOther.getId())
                        || (this.getId() != null
                                && castOther.getId() != null
                                && this.getId().equals(castOther.getId())))
                && (this.getVersion() == castOther.getVersion());
    }

    @Override
    public int hashCode() {
        int result = 17;

        result = 37 * result + (getId() == null ? 0 : this.getId().hashCode());
        result = 37 * result + (int) this.getVersion();

        return result;
    }

    // The following is extra code specified in src/main/config/templates
    // Adding extra-code
    private boolean comesFromExternalDataSource = false;
    private boolean selected;

    @jakarta.persistence.Transient
    public boolean isExternalDataSource() {
        return this.comesFromExternalDataSource;
    }

    public void setExternalDataSource(boolean value) {
        this.comesFromExternalDataSource = value;
    }

    @jakarta.persistence.Transient
    public boolean getSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void cleanPOJO() {
        // Clean entities objects with id
    }

    public void fillPOJO() {
        // Initialize all properties with new
    }

    public Persona createPOJO() {
        fillPOJO();

        return this;
    }
}

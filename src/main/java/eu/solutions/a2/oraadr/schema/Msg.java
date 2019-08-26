/**
 * Copyright (c) 2018-present, http://a2-solutions.eu
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */

package eu.solutions.a2.oraadr.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonFormat;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="txt" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *       &lt;attribute name="time" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
 *       &lt;attribute name="time_norm" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
 *       &lt;attribute name="org_id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="comp_id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="host_id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="host_addr" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="level" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" />
 *       &lt;attribute name="msg_id" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="group" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="client_id" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="module" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="pid" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="tid" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="user" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="inst_id" type="{http://www.w3.org/2001/XMLSchema}unsignedByte" />
 *       &lt;attribute name="detail_path" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="prob_key" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="upstream_comp" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="downstream_comp" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="ecid" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="errid" type="{http://www.w3.org/2001/XMLSchema}unsignedShort" />
 *       &lt;attribute name="rid" type="{http://www.w3.org/2001/XMLSchema}unsignedShort" />
 *       &lt;attribute name="err_seq" type="{http://www.w3.org/2001/XMLSchema}unsignedShort" />
 *       &lt;attribute name="version" type="{http://www.w3.org/2001/XMLSchema}unsignedByte" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "txt"
})
@XmlRootElement(name = "msg")
public class Msg {

	@JsonProperty("ADR_FILE_KEY")
	@XmlAttribute(name = "not_present_in_schema_001", required = false)
	protected String adrFileKey;
	@JsonProperty("MESSAGE_TEXT")
    @XmlElement(required = true)
    protected String txt;
	@JsonProperty("ORIGINATING_TIMESTAMP")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    @XmlAttribute(name = "time", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar time;
	@JsonProperty("NORMALIZED_TIMESTAMP")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    @XmlAttribute(name = "time_norm")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar timeNorm;
	@JsonProperty("ORGANIZATION_ID")
	@XmlAttribute(name = "org_id", required = true)
    protected String orgId;
	@JsonProperty("COMPONENT_ID")
    @XmlAttribute(name = "comp_id", required = true)
    protected String compId;
	@JsonProperty("HOST_ID")
    @XmlAttribute(name = "host_id", required = true)
    protected String hostId;
	@JsonProperty("HOST_ADDRESS")
    @XmlAttribute(name = "host_addr", required = true)
    protected String hostAddr;
	@JsonProperty("MESSAGE_TYPE")
    @XmlAttribute(name = "type")
    protected String type;
	@JsonProperty("MESSAGE_LEVEL")
    @XmlAttribute(name = "level")
    @XmlSchemaType(name = "unsignedInt")
    protected Long level;
	@JsonProperty("MESSAGE_ID")
    @XmlAttribute(name = "msg_id")
    protected String msgId;
	@JsonProperty("MESSAGE_GROUP")
    @XmlAttribute(name = "group")
    protected String group;
	@JsonProperty("CLIENT_ID")
	@XmlAttribute(name = "client_id")
    protected String clientId;
	@JsonProperty("MODULE_ID")
    @XmlAttribute(name = "module")
    protected String module;
	@JsonProperty("PROCESS_ID")
    @XmlAttribute(name = "pid", required = true)
    protected String pid;
	@JsonProperty("THREAD_ID")
    @XmlAttribute(name = "tid", required = true)
    protected String tid;
	@JsonProperty("USER_ID")
    @XmlAttribute(name = "user")
    protected String user;
	@JsonProperty("INSTANCE_ID")
    @XmlAttribute(name = "inst_id")
    @XmlSchemaType(name = "unsignedByte")
    protected Short instId;
	@JsonProperty("DETAILED_LOCATION")
    @XmlAttribute(name = "detail_path")
    protected String detailPath;
	@JsonProperty("PROBLEM_KEY")
    @XmlAttribute(name = "prob_key")
    protected String probKey;
	@JsonProperty("UPSTREAM_COMP_ID")
    @XmlAttribute(name = "upstream_comp")
    protected String upstreamComp;
	@JsonProperty("DOWNSTREAM_COMP_ID")
	@XmlAttribute(name = "downstream_comp")
    protected String downstreamComp;
	@JsonProperty("EXECUTION_CONTEXT_ID")
    @XmlAttribute(name = "ecid")
    protected String ecid;
	//TODO
    @XmlAttribute(name = "errid")
    @XmlSchemaType(name = "unsignedShort")
    protected Integer errid;
    //TODO
    @XmlAttribute(name = "rid")
    @XmlSchemaType(name = "unsignedShort")
    protected Integer rid;
    //TODO
    @XmlAttribute(name = "err_seq")
    @XmlSchemaType(name = "unsignedShort")
    protected Integer errSeq;
    @JsonProperty("VERSION")
    @XmlAttribute(name = "version")
    @XmlSchemaType(name = "unsignedByte")
    protected Short version;

    /**
     * Gets the value of the adrFileKey property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAdrFileKey() {
        return adrFileKey;
    }

    /**
     * Sets the value of the adrFileKey property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAdrFileKey(String value) {
        this.adrFileKey = value;
    }

    /**
     * Gets the value of the txt property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTxt() {
        return txt;
    }

    /**
     * Sets the value of the txt property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTxt(String value) {
        this.txt = value;
    }

    /**
     * Gets the value of the time property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getTime() {
        return time;
    }

    /**
     * Sets the value of the time property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setTime(XMLGregorianCalendar value) {
        this.time = value;
    }

    /**
     * Gets the value of the timeNorm property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getTimeNorm() {
        return timeNorm;
    }

    /**
     * Sets the value of the timeNorm property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setTimeNorm(XMLGregorianCalendar value) {
        this.timeNorm = value;
    }

    /**
     * Gets the value of the orgId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrgId() {
        return orgId;
    }

    /**
     * Sets the value of the orgId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrgId(String value) {
        this.orgId = value;
    }

    /**
     * Gets the value of the compId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCompId() {
        return compId;
    }

    /**
     * Sets the value of the compId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCompId(String value) {
        this.compId = value;
    }

    /**
     * Gets the value of the hostId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHostId() {
        return hostId;
    }

    /**
     * Sets the value of the hostId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHostId(String value) {
        this.hostId = value;
    }

    /**
     * Gets the value of the hostAddr property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHostAddr() {
        return hostAddr;
    }

    /**
     * Sets the value of the hostAddr property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHostAddr(String value) {
        this.hostAddr = value;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setType(String value) {
        this.type = value;
    }

    /**
     * Gets the value of the level property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getLevel() {
        return level;
    }

    /**
     * Sets the value of the level property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setLevel(Long value) {
        this.level = value;
    }

    /**
     * Gets the value of the msgId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMsgId() {
        return msgId;
    }

    /**
     * Sets the value of the msgId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMsgId(String value) {
        this.msgId = value;
    }

    /**
     * Gets the value of the group property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGroup() {
        return group;
    }

    /**
     * Sets the value of the group property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGroup(String value) {
        this.group = value;
    }

    /**
     * Gets the value of the clientId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClientId() {
        return clientId;
    }

    /**
     * Sets the value of the clientId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClientId(String value) {
        this.clientId = value;
    }

    /**
     * Gets the value of the module property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getModule() {
        return module;
    }

    /**
     * Sets the value of the module property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setModule(String value) {
        this.module = value;
    }

    /**
     * Gets the value of the pid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPid() {
        return pid;
    }

    /**
     * Sets the value of the pid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPid(String value) {
        this.pid = value;
    }

    /**
     * Gets the value of the tid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTid() {
        return tid;
    }

    /**
     * Sets the value of the tid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTid(String value) {
        this.tid = value;
    }

    /**
     * Gets the value of the user property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUser() {
        return user;
    }

    /**
     * Sets the value of the user property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUser(String value) {
        this.user = value;
    }

    /**
     * Gets the value of the instId property.
     * 
     * @return
     *     possible object is
     *     {@link Short }
     *     
     */
    public Short getInstId() {
        return instId;
    }

    /**
     * Sets the value of the instId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Short }
     *     
     */
    public void setInstId(Short value) {
        this.instId = value;
    }

    /**
     * Gets the value of the detailPath property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDetailPath() {
        return detailPath;
    }

    /**
     * Sets the value of the detailPath property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDetailPath(String value) {
        this.detailPath = value;
    }

    /**
     * Gets the value of the probKey property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProbKey() {
        return probKey;
    }

    /**
     * Sets the value of the probKey property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProbKey(String value) {
        this.probKey = value;
    }

    /**
     * Gets the value of the upstreamComp property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUpstreamComp() {
        return upstreamComp;
    }

    /**
     * Sets the value of the upstreamComp property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUpstreamComp(String value) {
        this.upstreamComp = value;
    }

    /**
     * Gets the value of the downstreamComp property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDownstreamComp() {
        return downstreamComp;
    }

    /**
     * Sets the value of the downstreamComp property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDownstreamComp(String value) {
        this.downstreamComp = value;
    }

    /**
     * Gets the value of the ecid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEcid() {
        return ecid;
    }

    /**
     * Sets the value of the ecid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEcid(String value) {
        this.ecid = value;
    }

    /**
     * Gets the value of the errid property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getErrid() {
        return errid;
    }

    /**
     * Sets the value of the errid property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setErrid(Integer value) {
        this.errid = value;
    }

    /**
     * Gets the value of the rid property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getRid() {
        return rid;
    }

    /**
     * Sets the value of the rid property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setRid(Integer value) {
        this.rid = value;
    }

    /**
     * Gets the value of the errSeq property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getErrSeq() {
        return errSeq;
    }

    /**
     * Sets the value of the errSeq property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setErrSeq(Integer value) {
        this.errSeq = value;
    }

    /**
     * Gets the value of the version property.
     * 
     * @return
     *     possible object is
     *     {@link Short }
     *     
     */
    public Short getVersion() {
        return version;
    }

    /**
     * Sets the value of the version property.
     * 
     * @param value
     *     allowed object is
     *     {@link Short }
     *     
     */
    public void setVersion(Short value) {
        this.version = value;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhan.dto;

/**
 *
 * @author weaversAndroid
 */
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "multicast_id",
    "success",
    "failure",
    "canonical_ids",
    "results"
})
public class PushNotificationResponseDto {

    @JsonProperty("multicast_id")
    private Long multicastId;
    @JsonProperty("success")
    private Integer success;
    @JsonProperty("failure")
    private Integer failure;
    @JsonProperty("canonical_ids")
    private Integer canonicalIds;
    @JsonProperty("results")
    private List<PushResult> results = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("multicast_id")
    public Long getMulticastId() {
        return multicastId;
    }

    @JsonProperty("multicast_id")
    public void setMulticastId(Long multicastId) {
        this.multicastId = multicastId;
    }

    @JsonProperty("success")
    public Integer getSuccess() {
        return success;
    }

    @JsonProperty("success")
    public void setSuccess(Integer success) {
        this.success = success;
    }

    @JsonProperty("failure")
    public Integer getFailure() {
        return failure;
    }

    @JsonProperty("failure")
    public void setFailure(Integer failure) {
        this.failure = failure;
    }

    @JsonProperty("canonical_ids")
    public Integer getCanonicalIds() {
        return canonicalIds;
    }

    @JsonProperty("canonical_ids")
    public void setCanonicalIds(Integer canonicalIds) {
        this.canonicalIds = canonicalIds;
    }

    @JsonProperty("results")
    public List<PushResult> getResults() {
        return results;
    }

    @JsonProperty("results")
    public void setResults(List<PushResult> results) {
        this.results = results;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}

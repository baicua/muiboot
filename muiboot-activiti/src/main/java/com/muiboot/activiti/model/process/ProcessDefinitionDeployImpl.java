package com.muiboot.activiti.model.process;

import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;

import java.util.Date;

/**
 * Created by 75631 on 2018/11/28.
 */
public class ProcessDefinitionDeployImpl implements ProcessDefinitionDeploy {
    private Date deploymentTime;
    private String id;
    private String category;
    private String key;
    private String name;
    private String description;
    private int version;
    private String resourceName;
    private String deploymentId;
    private String diagramResourceName;
    private boolean startFormKey;
    private boolean graphicalNotation;
    private boolean suspended;
    private String tenantId;
    public ProcessDefinitionDeployImpl(){

    }
    public ProcessDefinitionDeployImpl(ProcessDefinition processDefinition){
        createProcessDefinition(processDefinition);
    }
    public ProcessDefinitionDeployImpl(ProcessDefinition processDefinition, Deployment deployment){
        createProcessDefinition(processDefinition);
        if (null!=deployment){
            createDeployment(deployment);
        }
    }
    private void createProcessDefinition(ProcessDefinition processDefinition){
        id=processDefinition.getId();
        key=processDefinition.getKey();
        name=processDefinition.getName();
        category=processDefinition.getCategory();
        deploymentId=processDefinition.getDeploymentId();
        description=processDefinition.getDescription();
        version=processDefinition.getVersion();
        diagramResourceName=processDefinition.getDiagramResourceName();
        tenantId=processDefinition.getTenantId();
        resourceName=processDefinition.getResourceName();
        graphicalNotation=processDefinition.hasGraphicalNotation();
        startFormKey=processDefinition.hasStartFormKey();
        suspended=processDefinition.isSuspended();
    }
    private void createDeployment(Deployment deployment){
        deploymentTime=deployment.getDeploymentTime();
    }
    @Override
    public Date getDeploymentTime() {
        return deploymentTime;
    }

    public void setDeploymentTime(Date deploymentTime) {
        this.deploymentTime = deploymentTime;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @Override
    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    @Override
    public String getDeploymentId() {
        return deploymentId;
    }

    public void setDeploymentId(String deploymentId) {
        this.deploymentId = deploymentId;
    }

    @Override
    public String getDiagramResourceName() {
        return diagramResourceName;
    }

    public void setDiagramResourceName(String diagramResourceName) {
        this.diagramResourceName = diagramResourceName;
    }

    public boolean hasStartFormKey() {
        return startFormKey;
    }


    public void setStartFormKey(boolean startFormKey) {
        this.startFormKey = startFormKey;
    }

    public boolean hasGraphicalNotation() {
        return graphicalNotation;
    }

    public void setGraphicalNotation(boolean graphicalNotation) {
        this.graphicalNotation = graphicalNotation;
    }

    @Override
    public boolean isSuspended() {
        return suspended;
    }

    public void setSuspended(boolean suspended) {
        this.suspended = suspended;
    }

    @Override
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
}

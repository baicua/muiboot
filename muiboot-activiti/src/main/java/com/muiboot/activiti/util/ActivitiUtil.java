package com.muiboot.activiti.util;

import com.muiboot.activiti.entity.process.ProcessDefinitionDeploy;
import com.muiboot.activiti.entity.process.ProcessDefinitionDeployImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by 75631 on 2018/11/28.
 */
public class ActivitiUtil {
    public static List<ProcessDefinitionDeploy> createProcessDefinitionDeploy(List<Deployment> deployments,List<ProcessDefinition> processDefinitions){
        if (CollectionUtils.isEmpty(processDefinitions)){
            return null;
        }
        Map<String,ProcessDefinition> processDefinitionMap=processDefinitions.stream().collect(Collectors.toMap(ProcessDefinition::getDeploymentId, a -> a,(k1, k2)->k1));
        List<ProcessDefinitionDeploy> processDefinitionDeployImpls= new ArrayList<>();
        for (int i=0,l=deployments.size();i<l;i++){
            Deployment deployment=deployments.get(i);
            ProcessDefinition processDefinition=processDefinitionMap.get(deployment.getId());
            ProcessDefinitionDeployImpl processDefinitionDeploy=new ProcessDefinitionDeployImpl(processDefinition,deployment);
            processDefinitionDeployImpls.add(processDefinitionDeploy);
        }
        return processDefinitionDeployImpls;
    }
}

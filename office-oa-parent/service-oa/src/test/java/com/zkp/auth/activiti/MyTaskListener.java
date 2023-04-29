package com.zkp.auth.activiti;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

/**
 * 监听器分配任务
 */
public class MyTaskListener implements TaskListener {

    @Override
    public void notify(DelegateTask delegateTask) {
        if (delegateTask.getName().equals("经理审批")) {
            //这里指定任务负责人
            delegateTask.setAssignee("zhangsan03");
        } else if (delegateTask.getName().equals("人事审批")) {
            //这里指定任务负责人
            delegateTask.setAssignee("lisi03");
        }
    }
}

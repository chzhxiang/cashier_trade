<#assign shiro=JspTaglibs["/WEB-INF/tlds/shiro.tld"]/>
<#list subResources as resource>
	<@shiro.hasPermission name="${resource.code}"> 
         <#if resource.operationType==1>
            <a href="javascript:openUrl('${resource.value}');" class="operation-button-a">${resource.name}</a>
         <#elseif resource.operationType==2>
            <a href="javascript:${resource.value}();" class="operation-button-a">${resource.name}</a>
         </#if>
    </@shiro.hasPermission> 
</#list>     

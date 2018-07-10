<#assign shiro=JspTaglibs["/WEB-INF/tlds/shiro.tld"]/>
<ul>
 <#list childMenuList as childMenus>
       <@shiro.hasPermission name="${childMenus.code}"> 
          <li id='${childMenus.id}'><a href='#' onclick="openMenu('${childMenus.id}','${childMenus.value}')">${childMenus.name}</a></li>        
       </@shiro.hasPermission>
 </#list>
</ul>
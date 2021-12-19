<#import "parts/common.ftlh" as c>
<#import "parts/login.ftlh" as l>

<@c.page>
    <div>Загрузка данных</div>
<div>
    <@l.logout />
    <span><a href="/registration">Add new user</a></span>
</div>

<div>
    <form method="post" enctype="multipart/form-data">
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        <select type="text" name="text" placeholder="Введите сообщение">
            <option value="TT$FLAT_OFFERING_INT">Flat offering</option>
            <option value="TT$CHAR_OVERRIDE_INT">Characteristics override</option>
            <option value="TT$RELATION_OVERRIDE_INT">Relation_override</option>
        </select>
        <input type="file" name="file">
        <button type="submit">Отправить</button>
    </form>
</div>
    <table>

        <#if mes??>
            <#list mes as me>
               <p>${me}
            </#list>
        </#if>

    </table>

  <table style="text-align:left">
   <#if Tree??>
    <div>Дерево</div>
     <#list Tree as key>
        <p>${key}
      </#list>
    </#if>
    </table>
</@c.page>
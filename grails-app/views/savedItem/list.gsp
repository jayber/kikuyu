<%@ page import="kikuyu.domain.SavedItem" %>

<g:set var="entityName" value="${message(code: 'savedItem.label', default: 'SavedItem')}"/>
<g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]"/></g:link>
<div id="list-savedItem" class="content scaffold-list" role="main">
    <h1><g:message code="default.list.label" args="[entityName]"/></h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>
    <table>
        <thead>
        <tr>

            <th><g:message code="savedItem.folder.label" default="Folder"/></th>

            <g:sortableColumn property="name" title="${message(code: 'savedItem.name.label', default: 'Name')}"/>

            <g:sortableColumn property="url" title="${message(code: 'savedItem.url.label', default: 'Url')}"/>

        </tr>
        </thead>
        <tbody>
        <g:each in="${savedItemInstanceList}" status="i" var="savedItemInstance">
            <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

                <td><g:link action="show"
                            id="${savedItemInstance.id}">${fieldValue(bean: savedItemInstance, field: "folder")}</g:link></td>

                <td>${fieldValue(bean: savedItemInstance, field: "name")}</td>

                <td>${fieldValue(bean: savedItemInstance, field: "url")}</td>

            </tr>
        </g:each>
        </tbody>
    </table>

    <div class="pagination">
        <g:paginate total="${savedItemInstanceTotal}"/>
    </div>
</div>

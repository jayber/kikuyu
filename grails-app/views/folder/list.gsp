<%@ page import="kikuyu.domain.Folder" %>

<g:set var="entityName" value="${message(code: 'folder.label', default: 'Folder')}"/>

<div id="list-folder" class="content scaffold-list" role="main">
    <h1><g:message code="default.list.label" args="[entityName]"/></h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>
    <table>
        <thead>
        <tr>

            <g:sortableColumn property="name" title="${message(code: 'folder.name.label', default: 'Name')}"/>

        </tr>
        </thead>
        <tbody>
        <g:each in="${folderInstanceList}" status="i" var="folderInstance">
            <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

                <td><g:link action="show"
                            id="${folderInstance.id}">${fieldValue(bean: folderInstance, field: "name")}</g:link></td>

            </tr>
        </g:each>
        </tbody>
    </table>

    <div class="pagination">
        <g:paginate total="${folderInstanceTotal}"/>
    </div>
</div>

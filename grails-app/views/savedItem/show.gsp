<%@ page import="kikuyu.domain.SavedItem" %>

<g:set var="entityName" value="${message(code: 'savedItem.label', default: 'SavedItem')}"/>

<div id="show-savedItem" class="content scaffold-show" role="main">
    <h1><g:message code="default.show.label" args="[entityName]"/></h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>
    <ol class="property-list savedItem">

        <g:if test="${savedItemInstance?.folder}">
            <li class="fieldcontain">
                <span id="folder-label" class="property-label"><g:message code="savedItem.folder.label"
                                                                          default="Folder"/></span>

                <span class="property-value" aria-labelledby="folder-label"><g:link controller="folder" action="show"
                                                                                    id="${savedItemInstance?.folder?.id}">${savedItemInstance?.folder?.encodeAsHTML()}</g:link></span>

            </li>
        </g:if>

        <g:if test="${savedItemInstance?.name}">
            <li class="fieldcontain">
                <span id="name-label" class="property-label"><g:message code="savedItem.name.label"
                                                                        default="Name"/></span>

                <span class="property-value" aria-labelledby="name-label"><g:fieldValue bean="${savedItemInstance}"
                                                                                        field="name"/></span>

            </li>
        </g:if>

        <g:if test="${savedItemInstance?.url}">
            <li class="fieldcontain">
                <span id="url-label" class="property-label"><g:message code="savedItem.url.label" default="Url"/></span>

                <span class="property-value" aria-labelledby="url-label"><g:fieldValue bean="${savedItemInstance}"
                                                                                       field="url"/></span>

            </li>
        </g:if>

    </ol>
    <g:form>
        <fieldset class="buttons">
            <g:hiddenField name="id" value="${savedItemInstance?.id}"/>
            <g:link class="edit" action="edit" id="${savedItemInstance?.id}"><g:message code="default.button.edit.label"
                                                                                        default="Edit"/></g:link>
            <g:actionSubmit class="delete" action="delete"
                            value="${message(code: 'default.button.delete.label', default: 'Delete')}"
                            onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');"/>
        </fieldset>
    </g:form>
</div>

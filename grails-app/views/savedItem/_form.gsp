<%@ page import="kikuyu.domain.SavedItem" %>



<div class="fieldcontain ${hasErrors(bean: savedItemInstance, field: 'folder', 'error')} required">
    <label for="folder">
        <g:message code="savedItem.folder.label" default="Folder"/>
        <span class="required-indicator">*</span>
    </label>
    <g:select id="folder" name="folder.id" from="${kikuyu.domain.Folder.list()}" optionKey="id" required=""
              value="${savedItemInstance?.folder?.id}" class="many-to-one"/>
</div>

<div class="fieldcontain ${hasErrors(bean: savedItemInstance, field: 'name', 'error')} ">
    <label for="name">
        <g:message code="savedItem.name.label" default="Name"/>

    </label>
    <g:textField name="name" value="${savedItemInstance?.name}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: savedItemInstance, field: 'url', 'error')} ">
    <label for="url">
        <g:message code="savedItem.url.label" default="Url"/>

    </label>
    <g:textField name="url" value="${savedItemInstance?.url}"/>
</div>


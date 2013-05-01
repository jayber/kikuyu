<%@ page import="kikuyu.domain.Folder" %>



<div class="fieldcontain ${hasErrors(bean: folderInstance, field: 'name', 'error')} ">
    <label for="name">
        <g:message code="folder.name.label" default="Name"/>

    </label>
    <g:textField name="name" value="${folderInstance?.name}"/>
</div>


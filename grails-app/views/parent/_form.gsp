<%@ page import="com.holgergarcia.Parent" %>



<div class="fieldcontain ${hasErrors(bean: parentInstance, field: 'name', 'error')} required">
	<label for="name">
		<g:message code="parent.name.label" default="Name" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="name" required="" value="${parentInstance?.name}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: parentInstance, field: 'surname', 'error')} required">
	<label for="surname">
		<g:message code="parent.surname.label" default="Surname" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="surname" required="" value="${parentInstance?.surname}"/>

</div>


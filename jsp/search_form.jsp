<%--
  Created by IntelliJ IDEA.
  User: sergiu.indrie
  Date: 4/5/12
  Time: 4:08 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Search Criteria - XML Services JSP Client</title>

    <script type="text/javascript">

        function AddRule()
        {
        var $searchCriteriaForm = document.getElementById('searchCriteria');
        var $valueField, $typeField, $option, $anything;

        var $ruleCounter = document.getElementById('searchRuleIndex');
        $i = parseInt($ruleCounter.title) + 1;
        <%--update the number--%>
        $ruleCounter.title = '' + $i;

        <%--add new line--%>
        $anything = document.createElement('br');
        $searchCriteriaForm.appendChild($anything);

        <%--add the Type label--%>
        $anything = document.createElement('label');
        $anything.innerHTML = 'Type ';
        $searchCriteriaForm.appendChild($anything);

        <%--add type field--%>
        $typeField = document.createElement('select');
        $typeField.name = 'type' + $i;
        <%--add Attribute option--%>
        $option = document.createElement('option');
        $option.value = 'Attribute';
        $option.innerHTML = 'Attribute';
        $typeField.appendChild($option);
        <%--add Data option--%>
        $option = document.createElement('option');
        $option.value = 'Data';
        $option.innerHTML = 'Data';
        $typeField.appendChild($option);
        $searchCriteriaForm.appendChild($typeField);

        <%--add the Value label--%>
        $anything = document.createElement('label');
        $anything.innerHTML = ' Value ';
        $searchCriteriaForm.appendChild($anything);

        <%--add value field--%>
        $valueField = document.createElement('input');
        $valueField.name = 'value' + $i;
        $valueField.type = 'text';
        $searchCriteriaForm.appendChild($valueField);
        }

    </script>

</head>
<body>

<%--Include the app header--%>
<%@ include file="app_header.jsp" %>


<form action="search_result.jsp" method="POST">
    <div id="searchCriteria">
        <label>Type</label>
        <select name="type1">
            <option value="Attribute">Attribute</option>
            <option value="Data">Data</option>
        </select>

        <label>Value</label>
        <input name="value1" type="text"/>
    </div>
    <br>
    <input type="button" value="Add rule" onselect="AddRule" onclick="AddRule();">
    <input type="SUBMIT" value="Search">
</form>

<%--Holds the searchRule index (invisible)--%>
<div id="searchRuleIndex" title="1"/>

</body>
</html>



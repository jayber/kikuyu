<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>This is the template</title>
    <style type="text/css" media="screen">
    .slot {
        border: 1px dotted;
    }

    .template {
        background: lightblue;
    }

    .comp1 {
        background: lightgreen;
    }

    .comp2 {
        background: lightcoral;
    }
    </style>
</head>

<body>
<div class="template">
    template
    <div class="slot" location>should be #{replaced}</div> middle
    <div class="slot" location>should #{also} be replaced</div>
    after
</div>

</body>
</html>
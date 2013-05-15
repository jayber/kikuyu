<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>#{title}</title>
    <style type="text/css" media="screen">
    .slot {
        border: 1px dotted;
    }

    .slotHorizontal {
        width: 50%;
        float: left;
    }

    .template {
        background: lightblue;
    }

    .template2 {
        background: #DFDFDF;
    }

    .containerHorizontal {
        width: 100%;
    }

    .comp1 {
        background: lightgreen;
    }

    .comp2 {
        background: lightcoral;
    }

    .comp3 {
        background: mediumpurple;
    }
    </style>
</head>

<body>
<h1>#{title}</h1>

<div class="template">
    #{prologue}
    <div class="slot" location>should be</div>

    <div class="slot" location>should  be replaced</div>
    #{epilogue}
</div>

</body>
</html>
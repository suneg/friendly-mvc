<html>
    <head>
        <title>Friendly Example</h1>
    </head>
    <body>
        <form action="/example.web/Home/test" method="post">
        <h1>Friendly Example</h1>

        Parameter 1: ${view.parm1?if_exists}<br/>
        Parameter 2: ${view.parm2?if_exists}<br/>
        <br/><br/>

        Input 1: <input type="text" name="parm1" /><br/>
        Input 2: <input type="text" name="parm2" /><br/><br/>
        <input type="submit" value="Submit" />
        </form>
    </body>
</html>
<#macro debugger>
<#if debug??>
<a href="javascript:void(0);" id="show_debug" style="position:absolute;left:10px;top:10px;">Debug</a>
<fieldset id="debugger" style="display:none;">
    <legend>Debug</legend>
    <pre>
    PathInfo: ${debug.pathinfo}
    Controller: ${debug.controller}
    Action: ${debug.action}
    View: ${debug.view}
    Method: ${debug.method}
    QueryString: ${debug.querystring}

    Client Locale Preference: <#list debug.locales as locale>${locale.language},</#list>

    Session: ${debug.session?replace(',',',<br/>     ')}
    </pre>
</fieldset>
</#if>
</#macro>
<#assign oscache=JspTaglibs["/WEB-INF/tlds/oscache.tld"]/>
<@oscache.cache  time=0  scope="application"></@oscache.cache>
<!DOCTYPE html>
<html lang="en">
<head>
<meta name="renderer" content="webkit" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
<meta name="format-detection" content="telephone=no">
<style>
    @font-face {
        font-family: 'icomoon';
        src: url("${tradeBasePath}/trade/css/fonts/icomoon.eot");
        src: url("${tradeBasePath}/trade/css/fonts/icomoon.eot?") format("embedded-opentype"), url("${tradeBasePath}/trade/css/fonts/icomoon.ttf?") format("truetype"), url("${tradeBasePath}/trade/css/fonts/icomoon.woff?") format("woff"), url("${tradeBasePath}/trade/css/fonts/icomoon.svg?") format("svg");
        font-weight: normal;
        font-style: normal;
    }
</style>
<link rel="stylesheet" href="${tradeBasePath}/trade/css/style.css?v=${trade_js_version}" type="text/css">
<script type="text/javascript" src="${tradeBasePath}/trade/js/common/jquery-1.11.0.min.js?v=${trade_js_version}"></script>
<script type="text/javascript" src="${tradeBasePath}/trade/js/common/common.js?v=${trade_js_version}"></script>
<script type="text/javascript">
    //var ip='${hostIP}';
    var tradeBasePath = '${tradeBasePath}';
    /*var currentuser = {
    	userId  : '${platformUser.name}',
    	account : '${platformUser.account}',
    	name    : '${platformUser.name}'
    }*/
</script>

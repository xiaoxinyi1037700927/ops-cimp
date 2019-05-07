function IEVersion() {
    var userAgent = navigator.userAgent; //取得浏览器的userAgent字符串  
    var isIE = userAgent.indexOf("compatible") > -1 && userAgent.indexOf("MSIE") > -1; //判断是否IE<11浏览器  
    var isEdge = userAgent.indexOf("Edge") > -1 && !isIE; //判断是否IE的Edge浏览器  
    var isIE11 = userAgent.indexOf('Trident') > -1 && userAgent.indexOf("rv:11.0") > -1;
    if(isIE) {
        var reIE = new RegExp("MSIE (\\d+\\.\\d+);");
        reIE.test(userAgent);
        var fIEVersion = parseFloat(RegExp["$1"]);
        if(fIEVersion == 7) {
            return 7;
        } else if(fIEVersion == 8) {
            return 8;
        } else if(fIEVersion == 9) {
            return 9;
        } else if(fIEVersion == 10) {
            return 10;
        } else {
            return 6;//IE版本<=7
        }   
    } else if(isEdge) {
        return 'edge';//edge
    } else if(isIE11) {
        return 11; //IE11  
    }else{
        return -1;//不是ie浏览器
    }
};
if (IEVersion()>0&&IEVersion()<10) {
    location.href="/static/ifIE/browser.html";
}else{
    var loadingElement=document.createElement("div");
    var loadingHtml=[];
    loadingElement.setAttribute("id", "loading-wrapper");
    loadingHtml.push('<div class="loading-container">');
    loadingHtml.push('<div class="page_load">');
    loadingHtml.push('<div class="loader">');
    loadingHtml.push('</div><div class="loadText">进入');
    loadingHtml.push(document.title);
    loadingHtml.push('中...</div></div></div>');
    loadingElement.innerHTML = loadingHtml.join("");
    document.body.appendChild(loadingElement);
}
window.onload = function () {
    document.getElementById('loading-wrapper').style.display = 'none';
}
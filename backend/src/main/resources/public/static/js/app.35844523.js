(function(e){function t(t){for(var r,u,l=t[0],a=t[1],i=t[2],s=0,b=[];s<l.length;s++)u=l[s],Object.prototype.hasOwnProperty.call(o,u)&&o[u]&&b.push(o[u][0]),o[u]=0;for(r in a)Object.prototype.hasOwnProperty.call(a,r)&&(e[r]=a[r]);f&&f(t);while(b.length)b.shift()();return c.push.apply(c,i||[]),n()}function n(){for(var e,t=0;t<c.length;t++){for(var n=c[t],r=!0,u=1;u<n.length;u++){var a=n[u];0!==o[a]&&(r=!1)}r&&(c.splice(t--,1),e=l(l.s=n[0]))}return e}var r={},o={app:0},c=[];function u(e){return l.p+"static/js/"+({about:"about"}[e]||e)+"."+{about:"31f2ad1a"}[e]+".js"}function l(t){if(r[t])return r[t].exports;var n=r[t]={i:t,l:!1,exports:{}};return e[t].call(n.exports,n,n.exports,l),n.l=!0,n.exports}l.e=function(e){var t=[],n=o[e];if(0!==n)if(n)t.push(n[2]);else{var r=new Promise((function(t,r){n=o[e]=[t,r]}));t.push(n[2]=r);var c,a=document.createElement("script");a.charset="utf-8",a.timeout=120,l.nc&&a.setAttribute("nonce",l.nc),a.src=u(e);var i=new Error;c=function(t){a.onerror=a.onload=null,clearTimeout(s);var n=o[e];if(0!==n){if(n){var r=t&&("load"===t.type?"missing":t.type),c=t&&t.target&&t.target.src;i.message="Loading chunk "+e+" failed.\n("+r+": "+c+")",i.name="ChunkLoadError",i.type=r,i.request=c,n[1](i)}o[e]=void 0}};var s=setTimeout((function(){c({type:"timeout",target:a})}),12e4);a.onerror=a.onload=c,document.head.appendChild(a)}return Promise.all(t)},l.m=e,l.c=r,l.d=function(e,t,n){l.o(e,t)||Object.defineProperty(e,t,{enumerable:!0,get:n})},l.r=function(e){"undefined"!==typeof Symbol&&Symbol.toStringTag&&Object.defineProperty(e,Symbol.toStringTag,{value:"Module"}),Object.defineProperty(e,"__esModule",{value:!0})},l.t=function(e,t){if(1&t&&(e=l(e)),8&t)return e;if(4&t&&"object"===typeof e&&e&&e.__esModule)return e;var n=Object.create(null);if(l.r(n),Object.defineProperty(n,"default",{enumerable:!0,value:e}),2&t&&"string"!=typeof e)for(var r in e)l.d(n,r,function(t){return e[t]}.bind(null,r));return n},l.n=function(e){var t=e&&e.__esModule?function(){return e["default"]}:function(){return e};return l.d(t,"a",t),t},l.o=function(e,t){return Object.prototype.hasOwnProperty.call(e,t)},l.p="/",l.oe=function(e){throw console.error(e),e};var a=window["webpackJsonp"]=window["webpackJsonp"]||[],i=a.push.bind(a);a.push=t,a=a.slice();for(var s=0;s<a.length;s++)t(a[s]);var f=i;c.push([0,"chunk-vendors"]),n()})({0:function(e,t,n){e.exports=n("56d7")},3785:function(e,t,n){"use strict";n("ab23")},"49fc":function(e,t,n){"use strict";n("953c")},"56d7":function(e,t,n){"use strict";n.r(t);n("e260"),n("e6cf"),n("cca6"),n("a79d");var r=n("7a23"),o={id:"nav"},c=Object(r["e"])("Home"),u=Object(r["e"])(" | "),l=Object(r["e"])("About");function a(e,t){var n=Object(r["v"])("router-link"),a=Object(r["v"])("router-view");return Object(r["o"])(),Object(r["d"])(r["a"],null,[Object(r["f"])("div",o,[Object(r["f"])(n,{to:"/"},{default:Object(r["B"])((function(){return[c]})),_:1}),u,Object(r["f"])(n,{to:"/about"},{default:Object(r["B"])((function(){return[l]})),_:1})]),Object(r["f"])(a)],64)}n("3785");const i={};i.render=a;var s=i,f=(n("d3b7"),n("3ca3"),n("ddb0"),n("6c02")),b=n("cf05"),d=n.n(b),p={class:"home"},j=Object(r["f"])("img",{alt:"Vue logo",src:d.a},null,-1);function O(e,t,n,o,c,u){var l=Object(r["v"])("HelloWorld");return Object(r["o"])(),Object(r["d"])("div",p,[j,Object(r["f"])(l,{msg:"Welcome to Your Vue.js App"})])}n("b0c0");var h=Object(r["D"])("data-v-90579930");Object(r["r"])("data-v-90579930");var m={class:"hello"},v=Object(r["f"])("h3",null,"Load an image",-1),g=Object(r["f"])("br",null,null,-1),y=Object(r["f"])("br",null,null,-1),w=Object(r["f"])("br",null,null,-1),k=Object(r["f"])("br",null,null,-1),x=Object(r["f"])("br",null,null,-1),S=Object(r["f"])("br",null,null,-1),_=Object(r["f"])("img",{class:"img-display",style:{display:"block",margin:"20px auto"}},null,-1),P=Object(r["f"])("h3",null,"Upload a new image",-1),C=Object(r["f"])("br",null,null,-1),F=Object(r["f"])("br",null,null,-1),R=Object(r["e"])("File "),A=Object(r["f"])("br",null,null,-1),H=Object(r["f"])("br",null,null,-1);Object(r["p"])();var T=h((function(e,t,n,o,c,u){return Object(r["o"])(),Object(r["d"])("div",m,[Object(r["f"])("h1",null,Object(r["x"])(n.msg),1),v,g,y,Object(r["C"])(Object(r["f"])("select",{"onUpdate:modelValue":t[2]||(t[2]=function(t){return e.selected=t})},[(Object(r["o"])(!0),Object(r["d"])(r["a"],null,Object(r["u"])(c.response,(function(e){return Object(r["o"])(),Object(r["d"])("option",{key:e.id,value:e.id,onChange:t[1]||(t[1]=function(e){return u.download()})},Object(r["x"])(e.name),41,["value"])})),128))],512),[[r["z"],e.selected]]),w,k,Object(r["f"])("button",{onClick:t[3]||(t[3]=function(e){return u.download()})},"Load"),x,S,_,P,C,F,Object(r["f"])("label",null,[R,Object(r["f"])("input",{type:"file",id:"file",ref:"file",onChange:t[4]||(t[4]=function(e){return u.handleFileUpload()})},null,544)]),A,H,Object(r["f"])("button",{onClick:t[5]||(t[5]=function(e){return u.submitFile()})},"Submit")])})),L=n("1da1"),U=(n("96cf"),n("bc3a")),M=n.n(U),W={name:"HelloWorld",props:{msg:String},data:function(){return{file:"",response:[],errors:[]}},methods:{callRestService:function(){var e=this;M.a.get("images").then((function(t){e.response=t.data})).catch((function(t){e.errors.push(t)}))},download:function(){var e=this,t=document.querySelector(".img-display");M.a.get("images/"+this.selected,{responseType:"blob"}).then((function(e){var n=new window.FileReader;n.readAsDataURL(e.data),n.onload=function(){var e=n.result;t.setAttribute("src",e)}})).catch((function(t){e.errors.push(t)}))},handleFileUpload:function(){this.file=this.$refs.file.files[0]},submitFile:function(){var e=this,t=new FormData;t.append("file",this.file),M.a.post("/images",t,{headers:{"Content-Type":"multipart/form-data"}}).then((function(){e.callRestService()})).catch((function(t){e.errors.push(t)}))}},mounted:D};function D(){return E.apply(this,arguments)}function E(){return E=Object(L["a"])(regeneratorRuntime.mark((function e(){return regeneratorRuntime.wrap((function(e){while(1)switch(e.prev=e.next){case 0:this.callRestService(),this.download();case 2:case"end":return e.stop()}}),e,this)}))),E.apply(this,arguments)}n("49fc");W.render=T,W.__scopeId="data-v-90579930";var V=W,q={name:"Home",components:{HelloWorld:V}};q.render=O;var B=q,J=[{path:"/",name:"Home",component:B},{path:"/about",name:"About",component:function(){return n.e("about").then(n.bind(null,"f820"))}}],z=Object(f["a"])({history:Object(f["b"])("/"),routes:J}),I=z;Object(r["c"])(s).use(I).mount("#app")},"953c":function(e,t,n){},ab23:function(e,t,n){},cf05:function(e,t,n){e.exports=n.p+"static/img/logo.82b9c7a5.png"}});
//# sourceMappingURL=app.35844523.js.map
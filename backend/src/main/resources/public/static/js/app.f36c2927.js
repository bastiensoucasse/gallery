(function(e){function t(t){for(var n,c,o=t[0],l=t[1],s=t[2],d=0,p=[];d<o.length;d++)c=o[d],Object.prototype.hasOwnProperty.call(i,c)&&i[c]&&p.push(i[c][0]),i[c]=0;for(n in l)Object.prototype.hasOwnProperty.call(l,n)&&(e[n]=l[n]);u&&u(t);while(p.length)p.shift()();return r.push.apply(r,s||[]),a()}function a(){for(var e,t=0;t<r.length;t++){for(var a=r[t],n=!0,o=1;o<a.length;o++){var l=a[o];0!==i[l]&&(n=!1)}n&&(r.splice(t--,1),e=c(c.s=a[0]))}return e}var n={},i={app:0},r=[];function c(t){if(n[t])return n[t].exports;var a=n[t]={i:t,l:!1,exports:{}};return e[t].call(a.exports,a,a.exports,c),a.l=!0,a.exports}c.m=e,c.c=n,c.d=function(e,t,a){c.o(e,t)||Object.defineProperty(e,t,{enumerable:!0,get:a})},c.r=function(e){"undefined"!==typeof Symbol&&Symbol.toStringTag&&Object.defineProperty(e,Symbol.toStringTag,{value:"Module"}),Object.defineProperty(e,"__esModule",{value:!0})},c.t=function(e,t){if(1&t&&(e=c(e)),8&t)return e;if(4&t&&"object"===typeof e&&e&&e.__esModule)return e;var a=Object.create(null);if(c.r(a),Object.defineProperty(a,"default",{enumerable:!0,value:e}),2&t&&"string"!=typeof e)for(var n in e)c.d(a,n,function(t){return e[t]}.bind(null,n));return a},c.n=function(e){var t=e&&e.__esModule?function(){return e["default"]}:function(){return e};return c.d(t,"a",t),t},c.o=function(e,t){return Object.prototype.hasOwnProperty.call(e,t)},c.p="/";var o=window["webpackJsonp"]=window["webpackJsonp"]||[],l=o.push.bind(o);o.push=t,o=o.slice();for(var s=0;s<o.length;s++)t(o[s]);var u=l;r.push([0,"chunk-vendors"]),a()})({0:function(e,t,a){e.exports=a("56d7")},"1e67":function(e,t,a){},3666:function(e,t,a){},"561e":function(e,t,a){},"56d7":function(e,t,a){"use strict";a.r(t);a("e260"),a("e6cf"),a("cca6"),a("a79d");var n=a("7a23");function i(e,t){var a=Object(n["x"])("router-view");return Object(n["q"])(),Object(n["d"])(a)}a("6d4c");const r={};r.render=i;var c=r,o=a("6c02"),l={class:"home"},s=Object(n["h"])("header",null,[Object(n["h"])("h1",{class:"logo"},"Gallery")],-1);function u(e,t,a,i,r,c){var o=Object(n["x"])("gallery");return Object(n["q"])(),Object(n["d"])("div",l,[s,Object(n["h"])(o)])}a("b0c0");var d=Object(n["C"])("data-v-113e1418");Object(n["t"])("data-v-113e1418");var p={class:"gallery"},b={class:"gallery-wrapper"},h=Object(n["h"])("span",{class:"material-icons"},"add",-1);Object(n["r"])();var m=d((function(e,t,a,i,r,c){var o=Object(n["x"])("preview"),l=Object(n["x"])("importer");return Object(n["q"])(),Object(n["d"])("div",p,[Object(n["h"])("div",b,[(Object(n["q"])(!0),Object(n["d"])(n["a"],null,Object(n["w"])(r.response,(function(e){return Object(n["q"])(),Object(n["d"])("div",{key:e.id,onClick:function(t){return c.loadPreview(e.id,e.name,e.type,e.size)},class:"gallery-image"},[Object(n["h"])("img",{src:"/images/"+e.id},null,8,["src"])],8,["onClick"])})),128))]),Object(n["h"])("div",{class:"gallery-import",onClick:t[1]||(t[1]=function(){return c.loadImporter&&c.loadImporter.apply(c,arguments)})},[h]),-1!=r.id?(Object(n["q"])(),Object(n["d"])(o,{key:0,id:r.id,name:r.name,type:r.type,dimensions:r.dimensions,onClose:c.closePreview},null,8,["id","name","type","dimensions","onClose"])):Object(n["e"])("",!0),r.importing?(Object(n["q"])(),Object(n["d"])(l,{key:1,onClose:c.closeImporter},null,8,["onClose"])):Object(n["e"])("",!0)])})),v=(a("a9e3"),Object(n["C"])("data-v-4166526e"));Object(n["t"])("data-v-4166526e");var f={class:"preview-window"},O={class:"feature-box"},j=Object(n["h"])("span",{class:"material-icons"},"close",-1),y=Object(n["h"])("span",{class:"material-icons"},"delete",-1),g=Object(n["h"])("span",{class:"material-icons"},"download",-1),w=Object(n["h"])("span",{class:"material-icons"},"edit",-1),k=Object(n["h"])("span",{class:"material-icons-outlined"},"info",-1),C={class:"preview-visual"},q={id:"details-panel",class:"preview-features"},x=Object(n["h"])("h2",{class:"title"},"Details",-1),S={class:"preview-details"},P=Object(n["h"])("h3",{class:"category"},"ID",-1),I={class:"paragraph"},z=Object(n["h"])("h3",{class:"category"},"Name",-1),_={class:"paragraph"},T=Object(n["h"])("h3",{class:"category"},"Type",-1),E={class:"paragraph"},A=Object(n["h"])("h3",{class:"category"},"Dimensions",-1),D={class:"paragraph"},F={id:"edit-panel",class:"preview-features"},G=Object(n["h"])("h2",{class:"title"},"Edit",-1),$={class:"preview-details"},M=Object(n["h"])("h3",{class:"category"},"Convert to grayscale",-1),N=Object(n["h"])("input",{type:"hidden",name:"algorithm",value:"toGrayscale"},null,-1),R=Object(n["h"])("input",{class:"theme-button",type:"submit",value:"To grayscale"},null,-1),B=Object(n["h"])("h3",{class:"category"},"Apply a gain to the brightness",-1),H=Object(n["h"])("input",{type:"hidden",name:"algorithm",value:"changeBrightness"},null,-1),J=Object(n["h"])("input",{class:"preview-input",type:"number",name:"gain",min:"-255",max:"255",required:""},null,-1),L=Object(n["h"])("input",{class:"theme-button",type:"submit",value:"Change brightness"},null,-1),U=Object(n["h"])("h3",{class:"category"},"Colorize by setting the hue",-1),K=Object(n["h"])("input",{type:"hidden",name:"algorithm",value:"colorize"},null,-1),Q=Object(n["h"])("input",{class:"preview-input",type:"number",name:"hue",min:"0",max:"360",required:""},null,-1),V=Object(n["h"])("input",{class:"theme-button",type:"submit",value:"Colorize"},null,-1),W=Object(n["h"])("h3",{class:"category"},"Extend the dynamics",-1),X=Object(n["h"])("input",{type:"hidden",name:"algorithm",value:"extendDynamics"},null,-1),Y=Object(n["h"])("input",{class:"theme-button",type:"submit",value:"Extend dynamics"},null,-1),Z=Object(n["f"])('<h3 class="category" data-v-4166526e> Equalize the saturation or brightness histogram </h3><input type="hidden" name="algorithm" value="equalizeHistogram" data-v-4166526e><div class="choice" data-v-4166526e><input class="preview-radio" type="radio" name="channel" value="1" checked id="c1" data-v-4166526e><label for="c1" data-v-4166526e>Saturation</label><input class="preview-radio" type="radio" name="channel" value="2" id="c2" data-v-4166526e><label for="c2" data-v-4166526e>Brightness</label></div><input class="theme-button" type="submit" value="Equalize histogram" data-v-4166526e>',4),ee=Object(n["f"])('<h3 class="category" data-v-4166526e>Blur filter</h3><input class="preview-input" type="number" name="radius" min="1" max="10" required data-v-4166526e><div class="choice" data-v-4166526e><input class="preview-radio" type="radio" name="algorithm" value="meanFilter" id="mf" checked data-v-4166526e><label for="mf" data-v-4166526e>Mean</label><input class="preview-radio" type="radio" name="algorithm" value="gaussianFilter" id="gf" data-v-4166526e><label for="gf" data-v-4166526e>Gaussian</label></div><input class="theme-button" type="submit" value="Apply filter" data-v-4166526e>',4),te=Object(n["h"])("h3",{class:"category"},"Sobel operator",-1),ae=Object(n["h"])("input",{type:"hidden",name:"algorithm",value:"sobelOperator"},null,-1),ne=Object(n["h"])("input",{class:"theme-button",type:"submit",value:"Apply operator"},null,-1);Object(n["r"])();var ie=v((function(e,t,a,i,r,c){return Object(n["q"])(),Object(n["d"])("div",f,[Object(n["h"])("div",O,[Object(n["h"])("button",{class:"feature-link",onClick:t[1]||(t[1]=function(t){return e.$emit("close")})},[j]),Object(n["h"])("button",{class:"feature-link",onClick:t[2]||(t[2]=function(){return c.remove&&c.remove.apply(c,arguments)})},[y]),Object(n["h"])("button",{class:"feature-link",onClick:t[3]||(t[3]=function(){return c.download&&c.download.apply(c,arguments)})},[g]),Object(n["h"])("button",{class:"feature-link",onClick:t[4]||(t[4]=function(){return c.edit&&c.edit.apply(c,arguments)})},[w]),Object(n["h"])("button",{class:"feature-link",onClick:t[5]||(t[5]=function(){return c.details&&c.details.apply(c,arguments)})},[k])]),Object(n["h"])("div",C,[Object(n["h"])("img",{class:"preview-image",src:"/images/"+a.id,onError:t[6]||(t[6]=function(t){return e.$emit("close")})},null,40,["src"])]),Object(n["h"])("div",q,[x,Object(n["h"])("div",S,[Object(n["h"])("div",null,[P,Object(n["h"])("p",I,Object(n["z"])(a.id),1)]),Object(n["h"])("div",null,[z,Object(n["h"])("p",_,Object(n["z"])(a.name),1)]),Object(n["h"])("div",null,[T,Object(n["h"])("p",E,Object(n["z"])(a.type),1)]),Object(n["h"])("div",null,[A,Object(n["h"])("p",D,Object(n["z"])(a.dimensions),1)])])]),Object(n["h"])("div",F,[G,Object(n["h"])("div",$,[Object(n["h"])("form",{class:"preview-feature",method:"get",action:"/images/"+a.id},[M,N,R],8,["action"]),Object(n["h"])("form",{class:"preview-feature",method:"get",action:"/images/"+a.id},[B,H,J,L],8,["action"]),Object(n["h"])("form",{class:"preview-feature",method:"get",action:"/images/"+a.id},[U,K,Q,V],8,["action"]),Object(n["h"])("form",{class:"preview-feature",method:"get",action:"/images/"+a.id},[W,X,Y],8,["action"]),Object(n["h"])("form",{class:"preview-feature",method:"get",action:"/images/"+a.id},[Z],8,["action"]),Object(n["h"])("form",{class:"preview-feature",method:"get",action:"/images/"+a.id},[ee],8,["action"]),Object(n["h"])("form",{class:"preview-feature",method:"get",style:{"padding-bottom":"60px"},action:"/images/"+a.id},[te,ae,ne],8,["action"])])])])})),re=a("bc3a"),ce=a.n(re),oe={name:"Preview",props:{id:Number,name:String,type:String,dimensions:String},emits:{close:null},methods:{remove:function(){ce.a.delete("images/"+this.id).then((function(){location.reload()}))},download:function(){var e=this;ce.a.get("images/"+this.id,{responseType:"blob"}).then((function(t){var a=new window.FileReader;a.readAsDataURL(t.data),a.onload=function(){var t=document.createElement("a");t.href=a.result,t.setAttribute("download",e.name),document.body.appendChild(t),t.click()}}))},edit:function(){document.querySelector(".preview-window").style.gridTemplateColumns="100fr 350px",document.querySelector("#details-panel").style.display="none",document.querySelector("#edit-panel").style.display="block"},details:function(){document.querySelector(".preview-window").style.gridTemplateColumns="100fr 350px",document.querySelector("#edit-panel").style.display="none",document.querySelector("#details-panel").style.display="block"}}};a("b08c");oe.render=ie,oe.__scopeId="data-v-4166526e";var le=oe,se=Object(n["C"])("data-v-0b4348f5");Object(n["t"])("data-v-0b4348f5");var ue={class:"importer-window"},de={class:"importer-content"},pe={class:"feature-box"},be=Object(n["h"])("span",{class:"material-icons"},"close",-1),he=Object(n["h"])("h2",{class:"title"},"Import content",-1),me=Object(n["h"])("p",{class:"paragraph"},[Object(n["g"])(" To add images (JPEG or TIFF only) to your gallery,"),Object(n["h"])("br"),Object(n["g"])(" click on this button or drop your image on it. ")],-1),ve=Object(n["h"])("label",{class:"theme-button",for:"file"},"Choose an image",-1);Object(n["r"])();var fe=se((function(e,t,a,i,r,c){return Object(n["q"])(),Object(n["d"])("div",ue,[Object(n["h"])("div",de,[Object(n["h"])("div",pe,[Object(n["h"])("button",{class:"feature-link",onClick:t[1]||(t[1]=function(t){return e.$emit("close")})},[be])]),he,me,Object(n["h"])("input",{class:"importer-input",type:"file",id:"file",ref:"file",onChange:t[2]||(t[2]=function(){return c.upload&&c.upload.apply(c,arguments)}),multiple:""},null,544),ve])])})),Oe={name:"Importer",emits:{close:null},methods:{upload:function(){this.file=this.$refs.file.files[0];var e=new FormData;e.append("file",this.file),ce.a.post("/images",e,{headers:{"Content-Type":"multipart/form-data"}}).then((function(){location.reload()}))}},data:function(){return{file:""}}};a("6405");Oe.render=fe,Oe.__scopeId="data-v-0b4348f5";var je=Oe,ye={name:"Gallery",components:{Preview:le,Importer:je},methods:{callRestService:function(){var e=this;ce.a.get("images").then((function(t){if(e.response=t.data,e.$route.params.preview){var a=e.response[Number(e.$route.params.preview)];e.loadPreview(Number(a.id),a.name,a.type,a.size)}})).catch((function(t){e.errors.push(t)}))},loadPreview:function(e,t,a,n){this.id=Number(e),this.name=t,this.type=a,this.dimensions=n.replaceAll("*"," × ")},closePreview:function(){this.id=-1,this.name="",this.type="",this.dimensions=""},loadImporter:function(){this.importing=!0},closeImporter:function(){this.importing=!1}},data:function(){return{response:[],errors:[],id:-1,name:"",type:"",dimensions:"",importing:!1}},mounted:function(){this.callRestService()}};a("a4b5");ye.render=m,ye.__scopeId="data-v-113e1418";var ge=ye,we={name:"Home",components:{Gallery:ge}};we.render=u;var ke=we,Ce=[{path:"/",name:"Home",component:ke},{path:"/:preview",name:"Preview",component:ke}],qe=Object(o["a"])({history:Object(o["b"])("/"),routes:Ce}),xe=qe;Object(n["c"])(c).use(xe).mount("#app")},6405:function(e,t,a){"use strict";a("1e67")},"6d4c":function(e,t,a){"use strict";a("3666")},"72f9":function(e,t,a){},a4b5:function(e,t,a){"use strict";a("72f9")},b08c:function(e,t,a){"use strict";a("561e")}});
//# sourceMappingURL=app.f36c2927.js.map
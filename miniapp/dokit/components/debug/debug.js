"use strict";var img=require("../../utils/imgbase64");Component({data:{logs:[],tools:[]},lifetimes:{attached:function(){console.log("debug attached"),this.setData({tools:this.getTools()})}},methods:{getTools:function(){return[{type:"common",title:"常用工具",tools:[{title:"App信息",image:img.appinfoicon,type:"appinformation"},{title:"位置模拟",image:img.gpsicon,type:"positionsimulation"},{title:"缓存管理",image:img.saveicon,type:"storage"},{title:"H5任意门",image:img.h5dooricon,type:"h5door"},{title:"请求注射",image:img.injectoricon,type:"httpinjector"},{title:"更新版本",image:img.updateversionicon,type:"onUpdate"},{title:"数据\b模拟",image:img.apimockicon,type:"apimock"}]}]},onUpdate:function(){var e=wx.getUpdateManager();e.onCheckForUpdate(function(t){t.hasUpdate||wx.showModal({title:"更新提示",content:"当前已经是最新版本"})}),e.onUpdateReady(function(){wx.showModal({title:"更新提示",content:"新版本已经准备好，是否重启应用？",success:function(t){t.confirm&&e.applyUpdate()}})}),e.onUpdateFailed(function(){})},onToggle:function(t){var e=t.currentTarget.dataset.type;"onUpdate"===e?this[e]():this.triggerEvent("toggle",{componentType:e})},openSetting:function(){wx.openSetting({success:function(t){console.log(t)}})},onGoBack:function(){this.triggerEvent("toggle",{componentType:"dokit"})}}});
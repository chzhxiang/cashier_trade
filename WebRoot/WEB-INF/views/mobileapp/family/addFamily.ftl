         <form id="formBeanFamily" class="hf"> 
            <div class="boxcon pt70 pb0 ">
                <div class="list list-l-r mt8">
                    <p class="l-r-p">
                        <span class="middle">关系</span>
                        <select id="relations" onchange="turnRelations(this)" name="relation">
                            <option value="1">父亲</option> 
                            <option value="2">母亲</option> 
                            <option value="3">儿子</option> 
                            <option value="4">女儿</option> 
                            <option value="5">其他</option> 
                        </select>
                        <i class="icon icon-down middle icon-select-down"></i>
                    </p>
                    <p class="l-r-p" id="underAge" style="display: none;">
                        <span class="middle color-main">小于14岁</span>
                        <a class="iosCheckF" onclick="checkValue()">
                            <b class="iosCheck"><input id="checkValue" type="checkbox"><i class=""></i></b>
                        </a>
                    </p>
                </div>
                <div class="list list-l-r mt8">
                    <p class="l-r-p">
                        <span class="middle">姓名</span>
                        <input type="text" value="" name="fullname" id="fullname" class="list-input-type" placeholder="用于就医挂号，请填入真实姓名">
                        
                    </p>
                    <p class="l-r-p">
                        <span class="middle">头像</span>
                        <img class="list-img" src="${tradeBasePath}/mobileapp/images/women.png">
                    </p>
                    <p class="l-r-p">
                        <span class="middle">证件类型</span>
                        <select name="idType" id="idType">
                            <option value="" selected="selected">请选择</option> 
                            <option value="1">身份证</option>
                            <option value="2">港澳通行证</option> 
                            <option value="3">护照</option> 
                        </select>
                        <i class="icon icon-down middle icon-select-down"></i>
                    </p>
                    <p class="l-r-p">
                        <span class="middle">证件号</span>
                        <input type="text" value="" name="idCard" id="idCard" class="list-input-type" placeholder="请填入证件号">
                    </p>
                    <p class="l-r-p"><span class="middle">出生日期</span>
                        <input type="text"  id="birthdays" readonly class="list-input-type isdataControls needsclick" placeholder="请填写你的出生日期"/>
                    </p>
                    <p class="l-r-p" id="sexChoose">
                        <span class="middle">性别</span>
                        <label id="RadioGroup1_0" class="radio inline">
                            <i class="icon icon-radio"></i>
                            <input type="radio" value="1">男
                        </label>
                        <label id="RadioGroup1_1" class="radio inline">
                            <i class="icon icon-radio"></i>
                            <input type="radio" value="0">女
                        </label>
                    </p>
                    <p class="l-r-p">
                        <span class="middle">手机号码</span>
                        <input type="text" value="" id="mobile" name="mobile" class="list-input-type" placeholder="请输入手机号码" onblur="validateMobile(this);">
                    </p>
                   
                    <div id="guarder" style="display: none;">
	                    <p class="l-r-p">
	                        <span class="middle">监护人姓名</span>
	                        <input type="text" value="" name="guarderName" id="guarderName" class="list-input-type" placeholder="请填入真实姓名">
	                    </p>
	                    <p class="l-r-p">
	                        <span class="middle">监护人证件类</span>
	                        <select name="guarderCardtype" id="guarderCardtype">
	                        	<option value="">请选择</option>
	                            <option value="1">身份证</option>
	                            <option value="2">港澳通行证</option> 
	                            <option value="3">护照</option> 
	                        </select>
	                        <i class="icon icon-down middle icon-select-down"></i>
	                    </p>
	                    <p class="l-r-p">
	                        <span class="middle">监护人证件号</span>
	                         <input type="text" value="" name="guarderCardno" id="guarderCardno" class="list-input-type" placeholder="请填入证件号">
	                    </p>
	                    <p class="l-r-p">
	                        <span class="middle">监护人手机号码</span>
	                       <input type="text" value="" id="guarderMobile" name="guarderMobile" class="list-input-type" placeholder="请输入手机号码" onblur="validateMobile(this);">
	                    </p>
					</div>
					 <p class="l-r-p">
                        <span class="middle">地址</span>
                        <input type="text" value="" name="address" class="list-input-type" placeholder="请填详细地址">
                    </p>
                    <p class="l-r-p" id="defaultpatientCh">
                        <span class="middle color-main">设为默认就诊人</span>
                        <label class="checkbox">
                            <i class="icon icon-checkbox"></i>
                            <input class="checkbox-type" type="checkbox" value="">
                        </label>
                    </p>
                </div>
                <div class="button">
                    <a class="but-save" onclick="save()">确认</a>
                </div>
                <link href="${tradeBasePath}/mobileapp/css/mobiscroll/mobiscroll.css?v=${css_version}" rel="stylesheet">
                <script src="${tradeBasePath}/mobileapp/js/mobiscroll/mobiscroll.core-2.5.2.js?v=${js_version}" ></script>
                <script src="${tradeBasePath}/mobileapp/js/mobiscroll/mobiscroll.datetime-2.5.1.js?v=${js_version}"></script>
                <script src="${tradeBasePath}/mobileapp/js/family/family.js?v=${js_version}"></script>
                <script src="${tradeBasePath}/mobileapp/js/idcardValidate.js?v=${js_version}"></script>
            </div>
           
            <input type="hidden" id="age"  name="age" value="" />
            <input type="hidden" id="status"  name="status" value="1" />
            <input type="hidden" id="defaultpatient"  name="defaultpatient" value="" />
            <input type="hidden" id="sex"  name="sex" value="" />
            <input type="hidden" id="backUrl"  name="backUrl"  value="/mobileapp/toHomePage" />
		</form>
		<script>
			var tradeBasePath="${tradeBasePath}";
		</script>
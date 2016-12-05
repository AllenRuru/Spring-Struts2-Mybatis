var arrLoadedMenuId = new Array();
var arrMenu = new Array(
    new Array('01', '车险出单')
);

function StringBuffer() {
    this.__strings__ = new Array;
}

StringBuffer.prototype.append = function (str) {
    this.__strings__.push(str);
    return this;
};

StringBuffer.prototype.toString = function () {
    return this.__strings__.join("");
};

Array.prototype.put = function(value){
    var contain = false;
    for (var i = 0 ; i < this.length ; i++){
        if (this[i] == value){
            contain = true;
            break;
        }
    }
    if (! contain) {
        this.push(value);
    }
    return contain;
};

Array.prototype.del = function(value){
    for (var i = 0 ; i < this.length ; i++){
        if (this[i] == value){
            this.splice(i, 1);
        }
    }
};

function gfn_AjaxError(req, status, e) {
    if (status == "timeout") {
        gfn_Alert("您好！ 非常抱歉， 由于网络延时，我将暂无法提供服务，请稍后再试，谢谢！");
    } else {
        if (status == "error" && req.status == "500") {
            gfn_Alert("您好！ 非常抱歉， 由于网络延时，我将暂无法提供服务，请稍后再试，谢谢！");
        } else {
            var errContent = "";
            errContent += 'Error Code : ' + req.status + '\n';
            errContent += 'Message : ' + req.responseText + '\n';
            errContent += 'Error : ' + e.toString() + '\n';
            gfn_Alert(errContent);
        }
    }
}

function gfn_Alert(content, paramFunc) {
    swal({
        title: "提示"
        ,text: content
        ,html: true
    }, function() {
        if (typeof paramFunc != "undefined") {
            paramFunc();
        }
    });

    $('.sweet-alert').css('top', (($(parent.parent.window).scrollTop()) + (parent.parent.document.documentElement.clientHeight / 2)) - 110);
}

function gfn_Confirm(msg, paramFunc) {
    swal({
        title: "确认"
        ,text: msg
        ,type: "warning"
        ,showCancelButton: true
        ,cancelButtonText: "取消"
        ,confirmButtonColor: "#DD6B55"
        ,confirmButtonText: "确认"
        ,closeOnConfirm: true
    }, function() {
        if (typeof paramFunc != "undefined") {
            paramFunc();
        }
    });
    $('.sweet-alert').css('top', '50%');
}

function ShowLoading() {
    $(".loadingBg").show();
    $(".loadingCon").css('top', (($(parent.parent.window).scrollTop()) + (parent.parent.document.documentElement.clientHeight / 2)) - 60);
    $(".loadingCon").show();
}

function HideLoading() {
    $(".loadingBg,.loadingCon").hide();
}

function checkEmpty(val, rtn) {
    if (val == null || typeof val == 'undefined' || val == '') {
        if (typeof rtn == 'undefined') {
            return '';
        } else {
            return rtn;
        }
    } else {
        return val;
    }
}

function getDate2String(targetDate) {
    var year = targetDate.getFullYear();
    var month = targetDate.getMonth() + 1;
    var day = targetDate.getDate();
    var rtnVal;

    if (month < 10) month = "0" + month;
    if (day < 10) day = "0" + day;
    rtnVal = year + "-" + month + "-" + day;
    return rtnVal;
}

function getAddDate(dateVal, addVal) {
    var year = dateVal.split('-')[0];
    var month = dateVal.split('-')[1];
    var day = dateVal.split('-')[2];

    var dt = new Date(Number(year), Number(month) - 1, Number(day));
    var processTime = dt.getTime() + (Number(addVal) * 24 * 60 * 60 * 1000);

    var newDate = new Date();
    newDate.setTime(processTime);
    return getDate2String(newDate);
}

function isEmail(email) {
    if ($.trim(email) == "") {
        return false;
    }
    var regExp = /([\w-\.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([\w-]+\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\]?)$/;;
    if(!regExp.test(email)) {
        return false;
    }
    return true;
}

function isMobile(str){
    if ($.trim(str) == "") {
        return false;
    }
	var regu =/(^[1][3][0-9]{9}$)|(^[1][5][0-9]{9}$)|(^[1][8][0-9]{9}$)|(^14[5,7][0-9]{8}$)|(^17[0,6,7][0-9]{8}$)|(^[0][1-9]{1}[0-9]{9}$)/;
	var reg = new RegExp(regu);
	if (reg.test(str)) {
		return true;
	}else{
		return false;
	}
}

function changeTwoDecimal_f(x) {
    var f_x = parseFloat(x);
    if (isNaN(f_x)) {
        return false;
    }
    var f_x = Math.round(x*100)/100;
    var s_x = f_x.toString();
    var pos_decimal = s_x.indexOf('.');
    if (pos_decimal < 0) {
        pos_decimal = s_x.length;
        s_x += '.';
    }
    while (s_x.length <= pos_decimal + 2) {
        s_x += '0';
    }
    return s_x;
}

function isIdCardNo(num)
{
	var factorArr = new Array(7,9,10,5,8,4,2,1,6,3,7,9,10,5,8,4,2,1);
	var error;
	var varArray = new Array();
	var intValue;
	var lngProduct = 0;
	var intCheckDigit;
	var intStrLen = num.length;
	var idNumber = num;
	if ((intStrLen != 15) && (intStrLen != 18)) {
		return false;
	}
	for(var i = 0 ; i < intStrLen ; i++) {
		varArray[i] = idNumber.charAt(i);
		if ((varArray[i] < '0' || varArray[i] > '9') && (i != 17)) {
		return false;
		} else if (i < 17) {
		varArray[i] = varArray[i]*factorArr[i];
		}
	}
	if (intStrLen == 18) {
		var date8 = idNumber.substring(6,14);
		if (checkDate(date8) == false) {
		    return false;
		}
		for(i=0;i<17;i++) {
		    lngProduct = lngProduct + varArray[i];
		}
		intCheckDigit = 12 - lngProduct % 11;
		switch (intCheckDigit) {
			case 10:
			intCheckDigit = 'X';
			break;
			case 11:
			intCheckDigit = 0;
			break;
			case 12:
			intCheckDigit = 1;
			break;
		}
		// check last digit
		if (varArray[17].toUpperCase() != intCheckDigit) {
			return false;
		}
	}else{ //length is 15
		var date6 = idNumber.substring(6, 12);
		if (checkDate(date6) == false) {
		return false;
		}
	}
	return true;
}

function checkDate(date) {
	if(date.length == 8){
		date = date.substring(0,4) + "-" + date.substring(4,6) + "-" + date.substring(6);
	}
    if (! isDate(date, "-")) {
        return false;
    } else {
        return true;
    }
}

function isDate(date,splitChar) {
    var charSplit = (splitChar==null?"-":splitChar);
    var strValue = date.split(charSplit);

    if(strValue.length!=3) return false;
    if(!isInteger(strValue[0]) || !isInteger(strValue[1]) || !isInteger(strValue[2]) ) return false;

    var intYear  = Number(strValue[0]);
    var intMonth = Number(strValue[1]) - 1;
    var intDay   = Number(strValue[2]);

    var dt = new Date(intYear,intMonth,intDay);
    if (dt.getFullYear() != intYear ||
        dt.getMonth() != intMonth ||
        dt.getDate() != intDay) {
        return false;
    }
    return true;
}

function isInteger(strValue) {
    var result = regExpTest(strValue,/\d+/g);
    return result;
}

function regExpTest(source, re) {
    var result = false;
    if (source == null || source=="")
        return false;

    if (source == re.exec(source))
        result = true;

    return result;
}

function isLicenseNo(licenseNo) {
    var carPrefixs = [
                     "京", "津", "冀", "晋", "蒙", "辽", "吉", "黑", "沪", "苏", "浙", "皖", "闽",
                     "赣", "鲁", "豫", "鄂", "湘", "粤", "桂", "琼", "渝", "川", "贵", "云", "藏", "陕",
                     "甘", "青", "宁", "新"
                     ];
    if (licenseNo.length == 7
        && $.inArray(licenseNo.substring(0, 1), carPrefixs) >= 0
        && (/[a-zA-Z]/.test(licenseNo.charAt(1)))
        && (/^\s*[A-Za-z0-9]{5}\s*$/.test(licenseNo.substring(2, 7)))) {
        return true;
    } else {
        return false;
    }
}

function pressDatetime(e) {
    var value = String.fromCharCode(e.keyCode);
    if((value>=0 && value<=9) || value=="/" || value=="-" || value==":" || value==" ")
        return true;
    else
        return false;
}

function pressDecimal(e) {
    var value = String.fromCharCode(e.keyCode);
    if((value>=0 && value<=9) || value==".")
        return true;
    else
        return false;
}

function lfn_getBirthDayAndSexAndAge(identifyNo) {
    if (identifyNo.length == 18 && isIdCardNo(identifyNo)) {

        var birthdayValue = identifyNo.substring(6, 10) + "-" + identifyNo.substring(10, 12) + "-" + identifyNo.substring(12, 14);
        var briDay = new Date(identifyNo.substring(6, 10), Number(identifyNo.substring(10, 12)) - 1, identifyNo.substring(12, 14));

        var syArrDate;
        var syDate;
        if ($('#chkSYQuotation').is(':checked') && $.trim($('.divQuotation .syBoxDate .systartdate').val()) != "") {
            syArrDate = $.trim($('.divQuotation .syBoxDate .systartdate').val()).split('-');
            syDate = new Date(syArrDate[0], Number(syArrDate[1]) - 1, syArrDate[2]);
        } else {
            if ($('#chkJQQuotation').is(':checked') && $.trim($('.divQuotation .jqBoxDate .jqstartdate').val()) != "") {
                syArrDate = $.trim($('.divQuotation .jqBoxDate .jqstartdate').val()).split('-');
                syDate = new Date(syArrDate[0], Number(syArrDate[1]) - 1, syArrDate[2]);
            } else {
                syDate = new Date();
            }
        }

        var symd = ((syDate.getMonth() + 1) <= 9 ? "0" + (syDate.getMonth() + 1) : (syDate.getMonth() + 1)) + "" + (syDate.getDate() <= 9 ? "0" + syDate.getDate() : syDate.getDate());
        var age = (symd < identifyNo.substring(10, 14) ? syDate.getFullYear() - briDay.getFullYear() - 1 : syDate.getFullYear() - briDay.getFullYear());

        $(objBirthday).val(birthdayValue);
        var sex = 1;
        if (Number(identifyNo.charAt(16) / 2) * 2 != identifyNo.charAt(16)) {
            sex = 1;
        } else {
            sex = 2;
        }

        var arrReturn = new Array(identifyNo, birthdayValue, sex, age);
        return arrReturn;
    }
}

function lfn_setBirthDayAndSex(identifyNo, objBirthday, objSex) {
    if (identifyNo.length == 18 && isIdCardNo(identifyNo)) {

        var birthdayValue = identifyNo.substring(6, 10) + "-" + identifyNo.substring(10, 12) + "-" + identifyNo.substring(12, 14);
        var briDay = new Date(identifyNo.substring(6, 10), Number(identifyNo.substring(10, 12)) - 1, identifyNo.substring(12, 14));

        var syArrDate;
        var syDate;
        if ($('#chkSYQuotation').is(':checked') && $.trim($('.divQuotation .syBoxDate .systartdate').val()) != "") {
            syArrDate = $.trim($('.divQuotation .syBoxDate .systartdate').val()).split('-');
            syDate = new Date(syArrDate[0], Number(syArrDate[1]) - 1, syArrDate[2]);
        } else {
            if ($('#chkJQQuotation').is(':checked') && $.trim($('.divQuotation .jqBoxDate .jqstartdate').val()) != "") {
                syArrDate = $.trim($('.divQuotation .jqBoxDate .jqstartdate').val()).split('-');
                syDate = new Date(syArrDate[0], Number(syArrDate[1]) - 1, syArrDate[2]);
            } else {
                syDate = new Date();
            }
        }

        var symd = ((syDate.getMonth() + 1) <= 9 ? "0" + (syDate.getMonth() + 1) : (syDate.getMonth() + 1)) + "" + (syDate.getDate() <= 9 ? "0" + syDate.getDate() : syDate.getDate());
        var age = (symd < identifyNo.substring(10, 14) ? syDate.getFullYear() - briDay.getFullYear() - 1 : syDate.getFullYear() - briDay.getFullYear());

        $(objBirthday).val(birthdayValue);

        if (parseInt(identifyNo.charAt(16) / 2, 10) * 2 != identifyNo.charAt(16)) {
            $(objSex).eq(0).prop('checked', true);
        } else {
            $(objSex).eq(1).prop('checked', true);
        }
        return age;
    }

}

function lfn_getMenuName(menuId) {
    var rtnData = "";
    for (var i = 0 ; i < arrMenu.length ; i++) {
        if (arrMenu[i][0] == menuId) {
            rtnData = arrMenu[i][1];
            break;
        }
    }
    return rtnData;
}

function lfn_showMenu(menuId) {
    if (menuId != "01") {
        var minHeight = parent.parent.document.documentElement.clientHeight - 3;
        var sbIFrame = new StringBuffer();
        sbIFrame.append("<iframe src='' name='iFrameContent' id='iFrameContent' style='width:100%;height:100%;min-height:" + minHeight + "px;' scrolling=no frameborder='0'></iframe>");
        $('#con').html(sbIFrame.toString());

        if (menuId.length == 2) {
            $('#iFrameContent').attr('src', '/agency/menu/' + menuId + '/main.do');
        } else if (menuId.length == 4) {
            $('#iFrameContent').attr('src', '/agency/menu/' + menuId.substring(0, 2) + '/' + menuId + '/main.do');
        }
    } else {
        var sbIFrame = new StringBuffer();
        sbIFrame.append("<iframe src='' name='iFrameContent' id='iFrameContent' style='width:100%;height:714px;' scrolling=no frameborder='0'></iframe>");
        $('#con').html(sbIFrame.toString());

        $('#iFrameContent').attr('src', '/agency/menu/' + menuId + '/main.do');
/*
        if (! arrLoadedMenuId.put(menuId)) {
            $('.top_group .tap_warp ul.tapmenu li').removeClass('ui-state-active').addClass('ui-state-disabled');
            $('.top_group .tap_warp ul.tapmenu').append("<li class='menu_" + menuId + " ui-state-default ui-corner-top ui-tabs-active ui-state-active'><a href='javascript:lfn_ActiveMenu(\"" + menuId + "\")'>" + lfn_getMenuName(menuId) + "</a>&nbsp;&nbsp;<a href='javascript:lfn_CloseMenu(\"" + menuId + "\")'>X</a></li>");

            //$('.top_menu .contents_wrap .contents_box .page_tit .tit').html(lfn_getMenuName(menuId));

            var sbIFrame = new StringBuffer();
            sbIFrame.append("<iframe src='' name='iframe" + menuId + "' id='iframe" + menuId + "' style='width:100%' scrolling=no></iframe>");
            $('.body_contents .contents_wrap').append(sbIFrame.toString());
            $('.body_contents .contents_wrap iframe').hide();

            $('#iframe' + menuId).attr('src', '/agency/menu/' + menuId + '/main.do');
            //$('#iframe' + menuId).iframeHeight();

            $('#iframe' + menuId).load(function() {
                //$(this).css('height', $(this).contents().find('body').height() + 'px');
                $(this).css('height', window.innerHeight);
            });

            $('#iframe' + menuId).show();

        } else {
            lfn_ActiveMenu(menuId);
        }
*/
    }
}

var liSeq = 0;
function lfn_addQuotationMenu() {
    liSeq++;
    $('#wrap .topTab li:last').remove();
    $('#wrap .topTab li').removeClass('active');
    $('#wrap .topTab').append('<li class="liMenu_' + liSeq + ' active"><a onclick="javascript:lfn_ActiveMenu(\'' + liSeq + '\');">车险</a><i class="icon icon-close" onclick="javascript:lfn_CloseMenu(\'' + liSeq + '\');"></i></li>');

    var sbIFrame = new StringBuffer();
    sbIFrame.append("<iframe src='' name='iframe" + liSeq + "' id='iframe" + liSeq + "' style='width:100%' scrolling=no frameborder='0'></iframe>");
    $('#wrap .contents_wrap').append(sbIFrame.toString());
    $('#wrap .contents_wrap iframe').hide();

    $('#iframe' + liSeq).attr('src', '/agency/menu/01/proposal.do?seq=' + liSeq);
    $('#iframe' + liSeq).load(function() {
        //$(this).css('height', $(this).contents().find('body').height() + 'px');
        $(this).css('height', window.innerHeight);
    });
    $('#iframe' + liSeq).show();

    $('#wrap .topTab').append('<li class="tabPlus"><a onclick="javascript:lfn_addQuotationMenu()"><i class="icon icon-plus"></i></a></li>');
}

function resizeFrame() {
    var ifrm_height = $(document).find('body').innerHeight();
    parent.document.getElementById('iFrameContent').style.height = ifrm_height + "px";
}

function resizeFrame2(menuId) {
    var ifrm_height = Number($(document).find('body').innerHeight());
    parent.document.getElementById('iframe' + menuId).style.height = ifrm_height + "px";
}

function lfn_ActiveMenu(seq) {
    $('#wrap .topTab li').removeClass('active');
    $('#wrap .topTab .liMenu_' + seq).addClass('active');

    $('#wrap .contents_wrap iframe').hide();
    $('#iframe' + seq).show();
}

function lfn_CloseMenu(seq) {

    var paramFunc = function() {
        $('#wrap .topTab .liMenu_' + seq).remove();
        $('#iframe' + seq).remove();

        if ($('#wrap .topTab li').length > 1) {
            var newSeq = $('#wrap .topTab li').eq($('#wrap .topTab li').length - 2).attr('class').split('_')[1];
            lfn_ActiveMenu(newSeq);
        }
    };

    gfn_Confirm("是否删除此标签页？", paramFunc);

    //if (arrLoadedMenuId.length > 0) {
    //    $('.top_menu .contents_wrap .contents_box .page_tit .tit').html("");
    //} else {
    //    $('.top_menu .contents_wrap .contents_box .page_tit .tit').html("DashBoard");
    //}
}

function gfn_ServerDate() {
    var rtnDate = new Date();
    $.ajax({
        type: 'POST'
        //,url: window.location.href.toString()
        ,url: '/agency/index.jsp'
        //,async: true
        ,async: false
        ,success: function (data, status, xhr) {
            rtnDate = new Date(xhr.getResponseHeader('Date'));
        }
    });
    return rtnDate;
}


//$('input').attr('autocomplete', 'off');

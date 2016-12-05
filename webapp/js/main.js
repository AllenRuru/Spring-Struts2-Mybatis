$(function(){
    var selectedData = new Object();

    $(".SearchDiv .SearchInfoBtn").unbind('click').click(function(){
        selectedData = new Object();
        $('#footerBtn .btnUploadFile').removeClass('disabled').addClass('disabled');
        $('#footerBtn .btnQuotation').removeClass('disabled').addClass('disabled');
        $('#footerBtn .btnPrintValidateCar').removeClass('disabled').addClass('disabled');
        $('#footerBtn .btnPrintInsure').removeClass('disabled').addClass('disabled');
        $('#footerBtn .btnPrintEngages').removeClass('disabled').addClass('disabled');
        $('#footerBtn .btnPrintDevice').removeClass('disabled').addClass('disabled');
        $('#footerBtn .btnPrintProposalInfo').removeClass('disabled').addClass('disabled');
        $('#footerBtn .btnPrintInformed').removeClass('disabled').addClass('disabled');

        var params = {
            CREATE_TIME_START_QUERY: $(".SearchDiv .CREATE_TIME_START_QUERY").val()
            ,CREATE_TIME_END_QUERY: $(".SearchDiv .CREATE_TIME_END_QUERY").val()
            ,CUSTOMERTYPE: $(".SearchDiv .CustomerType").val()
            ,IDENTIFY_NO: $(".SearchDiv .IDENTIFY_NO").val()
            ,MOBILE: $(".SearchDiv .MOBILE").val()
            ,CAR_OWNER: $(".SearchDiv .CAR_OWNER").val()
            ,LICENSE_NO: $(".SearchDiv .LICENSE_NO").val()
            ,FRAME_NO: $(".SearchDiv .FRAME_NO").val()
            ,TEMPORARYNO: $(".SearchDiv .TEMPORARYNO").val()
            ,PROPOSAL_NO: $(".SearchDiv .PROPOSAL_NO").val()
            ,POLICYNO: $(".SearchDiv .POLICYNO").val()
        };

        ShowLoading();
        $.ajax({
            type: 'POST'
            ,url: contextRootPath + '/menu/02/search.do'
            ,data: params
            ,dataType: 'json'
            ,async: true
            ,success: function(data) {
                HideLoading();
                var sbHTML = new StringBuffer();
                if (data && data.length > 0) {
                    for (var i = 0 ; i < data.length ; i++) {
                        sbHTML.append("<tr> \n");
                        sbHTML.append("    <td><input type='radio' name='CheckSearchResultRadio' value='" + checkEmpty(data[i].TEMPORARYNO) + "' /> </td> \n");
                        sbHTML.append("    <td><a style='cursor:pointer' class='DetailInfoBtn detailTemporary'>" + checkEmpty(data[i].TEMPORARYNO) + "</a></td> \n");
                        sbHTML.append("    <td>" + checkEmpty(data[i].CUSTOMERTYPE_NAME, "&nbsp;") + "<br />" + checkEmpty(data[i].CAR_OWNER, "&nbsp;") + "</td> \n");
                        sbHTML.append("    <td>" + (data[i].NEWCAR_FLAG == "1" ? "新车" : checkEmpty(data[i].LICENSE_NO, "&nbsp;")) + "<br />" + checkEmpty(data[i].FRAME_NO, "&nbsp;") + "</td> \n");
                        sbHTML.append("    <td>" + checkEmpty(data[i].CREATE_TIME) + "</td> \n");
                        sbHTML.append("    <td>" + checkEmpty(data[i].PROPOSAL_NO_SY, "&nbsp;") + "<br />" + checkEmpty(data[i].PROPOSAL_NO_JQ, "&nbsp;") + "</td> \n");
                        sbHTML.append("    <td>" + checkEmpty(data[i].SY_SUM_PREMIUM, "&nbsp;") + "<br />" + checkEmpty(data[i].JQ_SUM_PREMIUM) + "(" + checkEmpty(data[i].SUMPAYTAX) + ")(元)" + "</td> \n");
                        sbHTML.append("    <td>" + checkEmpty(data[i].APPLICANT_INSURED_NAME) + "<br />" + checkEmpty(data[i].INSURED_NAME) + "</td> \n");
                        sbHTML.append("     <td>" + lfn_RtnUnderwriteName(checkEmpty(data[i].UNDERWRITEFLAG_SY, "&nbsp;")) + "<br />" + lfn_RtnUnderwriteName(checkEmpty(data[i].UNDERWRITEFLAG_JQ, "&nbsp;")) + "</td>    \n");
                        sbHTML.append("    <td>" + checkEmpty(data[i].PAYMETHOD_NAME, "&nbsp;") + "</td> \n");
                        sbHTML.append("    <td>" + checkEmpty(data[i].POLICYNO_SY, "&nbsp;") + "<br />" + checkEmpty(data[i].POLICYNO_JQ, "&nbsp;") + "</td> \n");
                        sbHTML.append("</tr> \n");
                    }
                    $(".SearchInfoDiv .search-result tbody").html(sbHTML.toString());

                    $('.SearchInfoDiv .search-result input[name=CheckSearchResultRadio]').each(function(idx, elem) {
                        $(elem).unbind('click').click(function(e) {
                            selectedData = data[idx];
                            if (checkEmpty(data[idx].PROPOSAL_NO_SY) == "" && checkEmpty(data[idx].PROPOSAL_NO_JQ) == "") {
                                $('#footerBtn .btnUploadFile').removeClass('disabled').addClass('disabled');
                            } else {
                                $('#footerBtn .btnUploadFile').removeClass('disabled');
                            }
                            if (checkEmpty(data[idx].POLICYNO_SY) == "" && checkEmpty(data[idx].POLICYNO_JQ) == "") {
                                $('#footerBtn .btnQuotation').removeClass('disabled');
                            } else {
                                $('#footerBtn .btnQuotation').removeClass('disabled').addClass('disabled');
                            }
                            if (checkEmpty(data[idx].POLICYNO_SY) == "" && checkEmpty(data[idx].POLICYNO_JQ) == "") {
                            	$('#footerBtn .btnPrintInsure').removeClass('disabled').addClass('disabled');
                            	$('#footerBtn .btnPrintEngages').removeClass('disabled').addClass('disabled');
                            	$('#footerBtn .btnPrintDevice').removeClass('disabled').addClass('disabled');
                            } else {
                            	$('#footerBtn .btnPrintInsure').removeClass('disabled');
                            	$('#footerBtn .btnPrintEngages').removeClass('disabled');
                            	$('#footerBtn .btnPrintDevice').removeClass('disabled');
                            }
                            $('#footerBtn .btnPrintProposalInfo').removeClass('disabled');
                            $('#footerBtn .btnPrintInformed').removeClass('disabled');

                            if (checkEmpty(data[idx].UNDERWRITEFLAG_SY, "") == "" && checkEmpty(data[idx].UNDERWRITEFLAG_JQ, "") == "" ) {
                                $('#footerBtn .btnPrintValidateCar').removeClass('disabled').addClass('disabled');
                            } else {
                                if ((checkEmpty(data[idx].UNDERWRITEFLAG_SY, "") == "" || /^1|^3|^8/.test(checkEmpty(data[idx].UNDERWRITEFLAG_SY, ""))) && (checkEmpty(data[idx].UNDERWRITEFLAG_JQ, "") == "" || /^1|^3|^8/.test(checkEmpty(data[idx].UNDERWRITEFLAG_JQ, "")))) {
                                    $('#footerBtn .btnPrintValidateCar').removeClass('disabled');
                                } else {
                                    $('#footerBtn .btnPrintValidateCar').removeClass('disabled').addClass('disabled');
                                }
                            }
                        });
                    });

                    $(".DetailInfoBtn").each(function(idx, elem) {
                        $(elem).unbind('click').click(function(e) {
                            var TEMPORARYNO = $(this).text();
                            var $frmSPCICommon = $("#frmSPCICommon");
                            if ($frmSPCICommon.length < 1) {
                                $frmSPCICommon = $("<form/>").attr({id:"frmSPCICommon", method:'POST'});
                                $(document.body).append($frmSPCICommon);
                            }
                            $frmSPCICommon.empty();
                            $frmSPCICommon.attr('target', '_blank');
                            $frmSPCICommon.attr('action', contextRootPath + '/menu/02/detail.do');

                            $("<input>").attr({type:"hidden", name:"TEMPORARYNO", value: TEMPORARYNO}).appendTo($frmSPCICommon);

                            $frmSPCICommon.submit();
                        });
                    });

                } else {
                    sbHTML.append("<tr> \n");
                    sbHTML.append("    <td colspan='11'>没有结果！</td> \n");
                    sbHTML.append("</tr> \n");
                    $(".SearchInfoDiv .search-result tbody").html(sbHTML.toString());
                }
            }
            ,error : function(req, status, e) {
                HideLoading();
                gfn_AjaxError(req, status, e);
            }
        });
    });

    $('.SearchDiv .input-daterange').datepicker({
        autoclose: true
    });

    $('.SearchDiv .btnReset').unbind('click').click(function(e) {
        $(".SearchDiv .CustomerType").val('');
        $(".SearchDiv .IDENTIFY_NO").val('');
        $(".SearchDiv .MOBILE").val('');
        $(".SearchDiv .CAR_OWNER").val('');
        $(".SearchDiv .LICENSE_NO").val('');
        $(".SearchDiv .FRAME_NO").val('');
        $(".SearchDiv .TEMPORARYNO").val('');
        $(".SearchDiv .PROPOSAL_NO").val('');
        $(".SearchDiv .POLICYNO").val('');
    });

    $(".footerBtn ul li.li01").mouseenter(function(){
        $(".ulList").css({"top":"-"+$(".ulList").height()+"px"}).show();
    });

    $(".footerBtn ul li.li01").mouseleave(function(){
        $(".ulList").hide();
    });

    var bExists = false;
    //打印特别约定清单
    $('.btnPrintEngages').unbind('click').click(function(elem){
        if ($('#footerBtn .btnPrintEngages').hasClass('disabled')) return;

        $("input:radio[name='CheckSearchResultRadio']").each(function(idx, elem){
            if($(elem).is(':checked')){
                bExists = true;
                var TEMPORARYNO = $(elem).parent().siblings().find('.detailTemporary').text();
                var $frmSPCICommon = $("#frmSPCICommon");
                if($frmSPCICommon.length < 1) {
                    $frmSPCICommon = $("<form/>").attr({id:"frmSPCICommon", method:'POST'});
                    $(document.body).append($frmSPCICommon);
                }
                $frmSPCICommon.empty();
                $frmSPCICommon.attr('target', '_blank');
                $frmSPCICommon.attr('action', contextRootPath + '/report/rpt01.do');

                $("<input>").attr({type:"hidden", name:"TEMPORARYNO", value: TEMPORARYNO}).appendTo($frmSPCICommon);

                $frmSPCICommon.submit();
            }
        });
        if (!bExists) {
            gfn_Alert('请选择要打印的对象！');
            return;
        }
    });

    //打印承保险别清单
    $('.btnPrintInsure').unbind('click').click(function(elem){
        if ($('#footerBtn .btnPrintInsure').hasClass('disabled')) return;

        $("input:radio[name='CheckSearchResultRadio']").each(function(idx, elem){
            if($(elem).is(':checked')){
                bExists = true;
                var TEMPORARYNO = $(elem).parent().siblings().find('.detailTemporary').text();
                var $frmSPCICommon = $("#frmSPCICommon");
                if($frmSPCICommon.length < 1) {
                    $frmSPCICommon = $("<form/>").attr({id:"frmSPCICommon", method:'POST'});
                    $(document.body).append($frmSPCICommon);
                }
                $frmSPCICommon.empty();
                $frmSPCICommon.attr('target', '_blank');
                $frmSPCICommon.attr('action', contextRootPath + '/report/rpt02.do');

                $("<input>").attr({type:"hidden", name:"TEMPORARYNO", value: TEMPORARYNO}).appendTo($frmSPCICommon);

                $frmSPCICommon.submit();
            }
        });
        if (!bExists) {
            gfn_Alert('请选择要打印的对象！');
            return;
        }
    });

    //打印新增设备明细表
    $('.btnPrintDevice').unbind('click').click(function(elem){
        if ($('#footerBtn .btnPrintDevice').hasClass('disabled')) return;

        $("input:radio[name='CheckSearchResultRadio']").each(function(idx, elem){
            if($(elem).is(':checked')){
                bExists = true;
                var TEMPORARYNO = $(elem).parent().siblings().find('.detailTemporary').text();
                var $frmSPCICommon = $("#frmSPCICommon");
                if($frmSPCICommon.length < 1) {
                    $frmSPCICommon = $("<form/>").attr({id:"frmSPCICommon", method:'POST'});
                    $(document.body).append($frmSPCICommon);
                }
                $frmSPCICommon.empty();
                $frmSPCICommon.attr('target', '_blank');
                $frmSPCICommon.attr('action', contextRootPath + '/report/rpt03.do');

                $("<input>").attr({type:"hidden", name:"TEMPORARYNO", value: TEMPORARYNO}).appendTo($frmSPCICommon);

                $frmSPCICommon.submit();
            }
        });
        if (!bExists) {
            gfn_Alert('请选择要打印的对象！');
            return;
        }
    });

    //打印车辆保险验车单
    $('.btnPrintValidateCar').unbind('click').click(function(elem){
        if ($('#footerBtn .btnPrintValidateCar').hasClass('disabled')) return;

        $("input:radio[name='CheckSearchResultRadio']").each(function(idx, elem){
            if($(elem).is(':checked')){
                bExists = true;
                var TEMPORARYNO = $(elem).parent().siblings().find('.detailTemporary').text();
                var $frmSPCICommon = $("#frmSPCICommon");
                if($frmSPCICommon.length < 1) {
                    $frmSPCICommon = $("<form/>").attr({id:"frmSPCICommon", method:'POST'});
                    $(document.body).append($frmSPCICommon);
                }
                $frmSPCICommon.empty();
                $frmSPCICommon.attr('target', '_blank');
                $frmSPCICommon.attr('action', contextRootPath + '/report/rpt04.do');

                $("<input>").attr({type:"hidden", name:"TEMPORARYNO", value: TEMPORARYNO}).appendTo($frmSPCICommon);

                $frmSPCICommon.submit();
            }
        });
        if (!bExists) {
            gfn_Alert('请选择要打印的对象！');
            return;
        }
    });

    //打印告知单
    $('.btnPrintInformed').unbind('click').click(function(elem){
        if ($('#footerBtn .btnPrintInformed').hasClass('disabled')) return;

        $("input:radio[name='CheckSearchResultRadio']").each(function(idx, elem){
            if($(elem).is(':checked')){
                bExists = true;
                var TEMPORARYNO = $(elem).parent().siblings().find('.detailTemporary').text();
                var $frmSPCICommon = $("#frmSPCICommon");
                if($frmSPCICommon.length < 1) {
                    $frmSPCICommon = $("<form/>").attr({id:"frmSPCICommon", method:'POST'});
                    $(document.body).append($frmSPCICommon);
                }
                $frmSPCICommon.empty();
                $frmSPCICommon.attr('target', '_blank');
                $frmSPCICommon.attr('action', contextRootPath + '/report/rpt06.do');

                $("<input>").attr({type:"hidden", name:"TEMPORARYNO", value: TEMPORARYNO}).appendTo($frmSPCICommon);

                $frmSPCICommon.submit();
            }
        });
        if (!bExists) {
            gfn_Alert('请选择要打印的对象！');
            return;
        }
    });

    //打印车辆保险投保单
    $('.btnPrintProposalInfo').unbind('click').click(function(elem){
        if ($('#footerBtn .btnPrintProposalInfo').hasClass('disabled')) return;

        $("input:radio[name='CheckSearchResultRadio']").each(function(idx, elem){
            if($(elem).is(':checked')){
                bExists = true;
                var TEMPORARYNO = $(elem).parent().siblings().find('.detailTemporary').text();
                var $frmSPCICommon = $("#frmSPCICommon");
                if($frmSPCICommon.length < 1) {
                    $frmSPCICommon = $("<form/>").attr({id:"frmSPCICommon", method:'POST'});
                    $(document.body).append($frmSPCICommon);
                }
                $frmSPCICommon.empty();
                $frmSPCICommon.attr('target', '_blank');
                $frmSPCICommon.attr('action', contextRootPath + '/report/rpt07.do');

                $("<input>").attr({type:"hidden", name:"TEMPORARYNO", value: TEMPORARYNO}).appendTo($frmSPCICommon);

                $frmSPCICommon.submit();
            }
        });
        if (!bExists) {
            gfn_Alert('请选择要打印的对象！');
            return;
        }
    });

    $('#footerBtn .btnUploadFile').unbind('click').click(function(e) {
        if ($('#footerBtn .btnUploadFile').hasClass('disabled')) return;

        var proposalNo = selectedData.PROPOSAL_NO_SY;
        if (proposalNo == "") {
            proposalNo = selectedData.PROPOSAL_NO_JQ;
        }
        $('#UploadFilePopup .photoBox').html('');
        var params = {
            PROPOSALNO: proposalNo
        };
        $.ajax({
            type: 'POST'
            ,url:  contextRootPath + '/upload/downProposalImage.do'
            ,data: params
            ,dataType: 'json'
            //,async: true
            ,async: false
            ,success: function(data) {
                if (data && data.ImageUrlDto && data.ImageUrlDto.length > 0 && Number(data.ImageUrlDto[0].serialNo) > 0) {
                    var sbHTML = new StringBuffer();
                    for (var i = 0 ; i < data.ImageUrlDto.length ; i++) {
                        sbHTML.append(" <div class='popup_thumb'>    \n");
                        sbHTML.append("     <p class='popup_thumb_img'><img src='" + data.ImageUrlDto[i].url + "' style='height: 150px; width: 150px;'></p>    \n");
                        sbHTML.append("     <p class='popup_thumb_txt'>" + lfn_rtnDocumentTypeName(data.ImageUrlDto[i].typeCode) + "</p>    \n");
                        sbHTML.append(" </div>    \n");
                    }
                    $('#UploadFilePopup .photoBox').html(sbHTML.toString());
                }
            }
            ,error: function(req, status, e) {
                gfn_AjaxError(req, status, e);
            }
        });

        easyDialog.open({
            container : 'UploadFilePopup',
            fixed : false
        });

        $('#UploadFilePopup a.btn_close').unbind('click').click(function(e) {
            easyDialog.close();
        });

        var uploader = WebUploader.create({
            auto: true
            ,method: 'POST'
            ,swf: contextRootPath + '/js/plugins/webuploader/Uploader.swf'
            ,server: contextRootPath + '/upload/proposalImage.do'
            ,pick: '#UploadFilePopup .btnUploadFile'
            ,resize: false
            ,fileVal: 'Filedata'
            ,accept: {
                title: 'Images'
                ,extensions: 'gif,jpg,jpeg,png'
                ,mimeTypes: 'image/gif,image/jpg,image/jpeg,image/png'
            }
            ,formData: { }
            ,fileNumLimit: 12
            ,fileSizeLimit: 200 * 1024 * 1024
            ,fileSingleSizeLimit: 2 * 1024 * 1024
        });

        uploader.on('startUpload', function() {
            uploader.option('formData', {
                DOCUMENTTYPECODE: $('#UploadFilePopup .DocumentTypeCode').val()
                ,PROPOSAL_NO_SY: selectedData.PROPOSAL_NO_SY
                ,PROPOSAL_NO_JQ: selectedData.PROPOSAL_NO_JQ
            });
        });

        uploader.on('uploadFinished', function() {
            $('#UploadFilePopup .photoBox').html('');
            var params = {
                PROPOSALNO: proposalNo
            };
            $.ajax({
                type: 'POST'
                ,url:  contextRootPath + '/upload/downProposalImage.do'
                ,data: params
                ,dataType: 'json'
                //,async: true
                ,async: false
                ,success: function(data) {
                    if (data && data.ImageUrlDto && data.ImageUrlDto.length > 0 && Number(data.ImageUrlDto[0].serialNo) > 0) {
                        var sbHTML = new StringBuffer();
                        for (var i = 0 ; i < data.ImageUrlDto.length ; i++) {
                            sbHTML.append(" <div class='popup_thumb'>    \n");
                            sbHTML.append("     <p class='popup_thumb_img'><img src='" + data.ImageUrlDto[i].url + "' style='height: 150px; width: 150px;'></p>    \n");
                            sbHTML.append("     <p class='popup_thumb_txt'>" + lfn_rtnDocumentTypeName(data.ImageUrlDto[i].typeCode) + "</p>    \n");
                            sbHTML.append(" </div>    \n");
                        }
                        $('#UploadFilePopup .photoBox').html(sbHTML.toString());
                    }
                }
                ,error: function(req, status, e) {
                    gfn_AjaxError(req, status, e);
                }
            });
            //gfn_Alert("上传成功" + queueData.uploadsSuccessful + "件, 失败" + queueData.uploadsErrored + "件");
        });
    });

    $('#footerBtn .btnQuotation').unbind('click').click(function(e) {
        if ($('#footerBtn .btnQuotation').hasClass('disabled')) return;

        var TEMPORARYNO = "";
        $(".SearchInfoDiv .search-result tbody input[name=CheckSearchResultRadio]").each(function(idx, elem) {
            if ($(elem).is(':checked')) {
                TEMPORARYNO = $(elem).val();
            }
        });

        if (TEMPORARYNO == "") {
            gfn_Alert('请选择对象！');
            return;
        }

        var sbIFrame = new StringBuffer();
        sbIFrame.append("<iframe src='' name='iFrameContent' id='iFrameContent' style='width:100%;height:714px;' scrolling=no frameborder='0'></iframe>");
        $('#con').html(sbIFrame.toString());

        var $frmSPCICommon = $("#frmSPCICommon");
        if($frmSPCICommon.length < 1) {
            $frmSPCICommon = $("<form/>").attr({id:"frmSPCICommon", method:'POST'});
            $(document.body).append($frmSPCICommon);
        }
        $frmSPCICommon.empty();
        $frmSPCICommon.attr('target', 'iFrameContent');
        $frmSPCICommon.attr('action', contextRootPath + '/menu/01/main.do');

        $("<input>").attr({type:"hidden", name:"TEMPORARYNO", value: TEMPORARYNO}).appendTo($frmSPCICommon);

        $frmSPCICommon.submit();
    });

    $(".SearchDiv .SearchInfoBtn").click();

    setInterval("resizeFrame()", 500);
});

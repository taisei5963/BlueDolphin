/**
 * jQueryのUIである「datepicker」でアイコン押下でカレンダー表示
 */

$(function(){
    $('.text_class').datepicker({
		//カレンダーアイコン画像
		buttonImage: "common/images/Calendar_icon.png",
		//ツールチップ表示文言
		buttonText: "カレンダーから選択",
		//画像として表示
		buttonImageOnly: true,
		showOn: "both",
		beforeShow: function() {
            $('.appendDatepicker1').append($('#ui-datepicker-div1'));
            $('.appendDatepicker2').append($('#ui-datepicker-div2'));
        }
	});

    //日本語化
    $.datepicker.regional['ja'] = {
        closeText: '閉じる',
        prevText: '<前',
        nextText: '次>',
        currentText: '今日',
        monthNames: ['1月','2月','3月','4月','5月','6月',
        '7月','8月','9月','10月','11月','12月'],
        monthNamesShort: ['1月','2月','3月','4月','5月','6月',
        '7月','8月','9月','10月','11月','12月'],
        dayNames: ['日曜日','月曜日','火曜日','水曜日','木曜日','金曜日','土曜日'],
        dayNamesShort: ['日','月','火','水','木','金','土'],
        dayNamesMin: ['日','月','火','水','木','金','土'],
        weekHeader: '週',
        dateFormat: 'yy/m/d',
        firstDay: 0,
        isRTL: false,
        showMonthAfterYear: true,
        yearSuffix: '年'};
    $.datepicker.setDefaults($.datepicker.regional['ja']);
});
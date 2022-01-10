/**
 * 現在時刻のデジタル表示処理
 */

/**
 * 「時」に関する時間セットを行うメソッド
 */
function setTimeHour(num) {
	// 桁数が1桁だったら先頭に0を加えて2桁に調整する
	var ret = num;
	return ret;
}

/**
 * 「分」に関する時間セットを行うメソッド
 */
function setTimeMinutes(num) {
	// 桁数が1桁だったら先頭に0を加えて2桁に調整する
	var ret;
	if (num < 10) {
		ret = "0" + num;
	} else {
		ret = num;
	}
	return ret;
}

function showDigitalClock() {
	var nowTime = new Date();
	var nowHour = setTimeHour(nowTime.getHours());
	var nowMinutes = setTimeMinutes(nowTime.getMinutes());
	var format = nowHour + ":" + nowMinutes;
	document.getElementById("CurrentRealTime").innerHTML = format;
}
setInterval('showDigitalClock()', 1000);
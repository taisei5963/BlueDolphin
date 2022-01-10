/**
 * プルダウンの選択肢で画面遷移を行う
 */
window.onload = function(){
	document.getElementById('select').onchange = function(){
		var url = this.options[this.selectedIndex].value;
		if(url != ''){
			document.pulldownMonth.submit();
		}
	};
};
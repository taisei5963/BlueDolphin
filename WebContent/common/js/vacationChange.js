/**
 * プルダウンの選択肢で画面遷移を行う
 */
window.onload = function(){
	document.getElementById('vacation').onchange = function(){
		var url = this.options[this.selectedIndex].value;
		if(url != ''){
			document.vacateForm.submit();
		}
	};
};
/**
 * プルダウンの選択肢で画面遷移を行う
 */
window.onload = function(){
	document.getElementById('filter').onchange = function(){
		var url = this.options[this.selectedIndex].value;
		if(url != ''){
			document.processForm.submit();
		}
	};
};
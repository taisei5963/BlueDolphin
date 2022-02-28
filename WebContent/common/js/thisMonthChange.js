/**
 * プルダウンの選択肢で画面遷移を行う
 */
window.onload = function(){
	document.getElementById('display_date_id').onchange = function(){
		var url = this.options[this.selectedIndex].value;
		if(url != ''){
			document.display_date_form.submit();
		}
	};
};
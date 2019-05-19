<?php

	file_put_contents(
		"/var/www/html/asofterspaceen/qrimagewriter/converter/input.json",
		"{" .
		'"baseurl": "http://www.ideanding.com/etiquetadora/?id=",' .
		'"id": "lxxl213473",' .
		'"picture": "' . $_FILES['picture']['tmp_name'] . '",' .
		'"logo": "logo.png",' .
		'"width": ' . $_POST['width'] . ',' .
		'"height": ' . $_POST['height'] .
		'}'
	);

	exec("converter/run.sh");

?>

Here is your picture:

<img src="https://www.asofterspace.com/qrimagewriter/converter/output.png" />

<?php
	$download_directly = false;

	if ($_SERVER['REQUEST_METHOD'] === 'POST') {
		$orig_input_filename = $_FILES['picture']['name'];
		$extension = pathinfo($orig_input_filename, PATHINFO_EXTENSION);
		$file_identifier = pathinfo($orig_input_filename, PATHINFO_FILENAME);
		$input_path = '/var/www/html/asofterspaceen/qrimagewriter/converter/input_' . $file_identifier . '.' . $extension;
		move_uploaded_file($_FILES['picture']['tmp_name'], $input_path);
		$width = $_POST['width'];
		$height = $_POST['height'];
		$id = "lxxl213473";
	} else {
		$input_path = $_GET['picture'];
		$extension = pathinfo($input_path, PATHINFO_EXTENSION);
		$file_identifier = pathinfo($input_path, PATHINFO_FILENAME);
		$width = $_GET['width'];
		$height = $_GET['height'];
		$id = $_GET['id'];
		if (isset($_GET['down']) && ($_GET['down'] == 1)) {
			$download_directly = true;
		}
		if (isset($_GET['DOWN']) && ($_GET['DOWN'] == 1)) {
			$download_directly = true;
		}
	}

	file_put_contents(
		"/var/www/html/asofterspaceen/qrimagewriter/converter/input.json",
		"{" .
		'"baseurl": "http://www.ideanding.com/etiquetadora/?id=",' .
		'"id": "' . $id . '",' .
		'"picture": "' . $input_path . '",' .
		'"logo": "logo.png",' .
		'"width": ' . $width . ',' .
		'"height": ' . $height .
		'}'
	);

	exec("/var/www/html/asofterspaceen/qrimagewriter/converter/run.sh");

	$output_name = $id . '.png';
	$output_file = "/var/www/html/asofterspaceen/qrimagewriter/converter/" . $output_name;

	rename("/var/www/html/asofterspaceen/qrimagewriter/converter/output.png", $output_file);

	if ($download_directly) {

		header('Expires: 0');
		header('Last-Modified: ' . gmdate('D, d M Y H:i:s') . ' GMT');
		header('Cache-Control: no-store, no-cache, must-revalidate');
		header('Pragma: no-cache');
		header('Content-type: image/png');
		header('Content-length: ' . filesize($output_file));
		header('Content-Disposition: attachment; filename="' . $output_name . '"');
		readfile($output_file);
		exit;

	} else {

		echo('<html>');
		echo('<head>');
		echo('</head>');
		echo('<body>');
		echo('Here is your picture:<br><br>');
		echo('<img src="https://www.asofterspace.com/qrimagewriter/converter/' . $output_name . '" />');
		echo('</body>');
	}
?>

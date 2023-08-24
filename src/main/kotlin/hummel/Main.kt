package hummel

import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.EventQueue
import java.awt.GridLayout
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import javax.swing.*
import javax.swing.border.EmptyBorder

fun main() {
	EventQueue.invokeLater {
		try {
			for (info in UIManager.getInstalledLookAndFeels()) {
				if ("Windows Classic" == info.name) {
					UIManager.setLookAndFeel(info.className)
					break
				}
			}
			val frame = GUI()
			frame.isVisible = true
		} catch (e: Exception) {
			e.printStackTrace()
		}
	}
}

class GUI : JFrame() {
	private fun selectPath(pathField: JTextField) {
		val fileChooser = JFileChooser()
		val result = fileChooser.showOpenDialog(this)
		if (result == JFileChooser.APPROVE_OPTION) {
			pathField.text = fileChooser.selectedFile.absolutePath
		}
	}

	private fun isEqual(firstFile: Path, secondFile: Path): Boolean {
		if (Files.size(firstFile) != Files.size(secondFile)) {
			return false
		}
		val first = Files.readAllBytes(firstFile)
		val second = Files.readAllBytes(secondFile)
		return first.contentEquals(second)
	}

	private fun process(inputField: JTextField, outputField: JTextField) {
		if (inputField.text.isEmpty() || outputField.text.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Fill the fields", "Error", JOptionPane.ERROR_MESSAGE)
			return
		}
		val firstFile = File(inputField.text)
		val secondFile = File(outputField.text)
		if (!firstFile.exists() || !firstFile.isFile || !secondFile.exists() || !secondFile.isFile) {
			JOptionPane.showMessageDialog(this, "Corrupted file", "Error", JOptionPane.ERROR_MESSAGE)
			return
		}
		val isEqual = isEqual(firstFile.toPath(), secondFile.toPath())
		if (isEqual) {
			JOptionPane.showMessageDialog(
				this, "Files are equal", "Message", JOptionPane.INFORMATION_MESSAGE
			)
		} else {
			JOptionPane.showMessageDialog(
				this, "Files are not equal", "Message", JOptionPane.INFORMATION_MESSAGE
			)
		}
	}

	init {
		title = "File Comparator"
		defaultCloseOperation = EXIT_ON_CLOSE
		setBounds(100, 100, 550, 180)

		val contentPanel = JPanel()
		contentPanel.border = EmptyBorder(5, 5, 5, 5)
		contentPanel.layout = BorderLayout(0, 0)
		contentPanel.layout = GridLayout(0, 1, 0, 0)
		contentPane = contentPanel

		val inputPanel = JPanel()
		val inputLabel = JLabel("Input path:")
		inputLabel.preferredSize = Dimension(80, inputLabel.preferredSize.height)
		val inputField = JTextField(24)
		val inputButton = JButton("Select path")
		inputButton.addActionListener { selectPath(inputField) }
		inputPanel.add(inputLabel)
		inputPanel.add(inputField)
		inputPanel.add(inputButton)

		val outputPanel = JPanel()
		val outputLabel = JLabel("Output path:")
		outputLabel.preferredSize = Dimension(80, outputLabel.preferredSize.height)
		val outputField = JTextField(24)
		val outputButton = JButton("Select path")
		outputButton.addActionListener { selectPath(outputField) }
		outputPanel.add(outputLabel)
		outputPanel.add(outputField)
		outputPanel.add(outputButton)

		val processPanel = JPanel()
		val processButton = JButton("Process")
		processButton.addActionListener {
			process(inputField, outputField)
		}
		processPanel.add(processButton)

		contentPanel.add(inputPanel)
		contentPanel.add(outputPanel)
		contentPanel.add(processPanel)

		setLocationRelativeTo(null)
	}
}
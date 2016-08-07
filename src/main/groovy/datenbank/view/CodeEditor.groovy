package datenbank.view

import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;

/**
 * A syntax highlighting code editor for JavaFX created by wrapping a
 * CodeMirror code editor in a WebView.
 *
 * See http://codemirror.net for more information on using the codemirror editor.
 */
public class CodeEditor extends VBox {
  /** a webview used to encapsulate the CodeMirror JavaScript. */
  final WebView webview = new WebView();
  /** a snapshot of the code to be edited kept for easy initilization and reversion of editable code. */
  private String editingCode;

  /**
   * a template for editing code - this can be changed to any template derived from the
   * supported modes at http://codemirror.net to allow syntax highlighted editing of
   * a wide variety of languages.
   */
  private String editingTemplate =
    "<!doctype html>" +
    "<html>" +
    "<head>" +
    "  <link rel=\"stylesheet\" href=\""+getClass().getResource("codemirror/lib/codemirror.css")+"\">" +
    "  <script src=\"http://codemirror.net/lib/codemirror.js\"></script>" +
    "  <script src=\""+getClass().getResource("codemirror/mode/sql/sql.js")+"\"></script>" +
    "</head>" +
    "<body>" +
    "<form><textarea id=\"code\" name=\"code\">\n" +
    "[code]" +
    "</textarea></form>" +
    "<script>" +
    "  var editor = CodeMirror.fromTextArea(document.getElementById(\"code\"), {" +
    "    lineNumbers: true," +
    "    matchBrackets: true," +
    "    mode: \"text/x-mssql\"" +
    "  });" +
    "</script>" +
    "</body>" +
    "</html>";
	
private String editingTemplateJava =
	"<!doctype html>" +
	"<html>" +
	"<head>" +
	"  <link rel=\"stylesheet\" href=\""+getClass().getResource("codemirror/lib/codemirror.css")+"\">" +
	"  <script src=\"http://codemirror.net/lib/codemirror.js\"></script>" +
	"  <script src=\""+getClass().getResource("codemirror/mode/clike/clike.js")+"\"></script>" +
	"</head>" +
	"<body>" +
	"<form><textarea id=\"code\" name=\"code\">\n" +
	"[code]" +
	"</textarea></form>" +
	"<script>" +
	"  var editor = CodeMirror.fromTextArea(document.getElementById(\"code\"), {" +
	"    lineNumbers: true," +
	"    matchBrackets: true," +
	"    mode: \"text/x-java\"" +
	"  });" +
	"</script>" +
	"</body>" +
	"</html>";

  /** applies the editing template to the editing code to create the html+javascript source for a code editor. */
  private String applyEditingTemplateSQL() {
	  editingTemplate.replace("[code]", editingCode)
  }
  
  private String applyEditingTemplateJava() {
	  editingTemplateJava.replace("[code]", editingCode)
  }	



  /** returns the current code in the editor and updates an editing snapshot of the code which can be reverted to. */
  public String getCodeAndSnapshot() {
    this.editingCode = (String ) webview.getEngine().executeScript("editor.getValue();");
    return editingCode;
  }

  /**
   * Create a new code editor.
   * @param editingCode the initial code to be edited in the code editor.
   */
  CodeEditor(String editingCode, String type) {
	  
 
    this.editingCode = editingCode;

    webview.setPrefSize(650, 325);
    webview.setMinSize(650, 325);
	if(type == "SQL")
   		webview.getEngine().loadContent(applyEditingTemplateSQL());
	else
		webview.getEngine().loadContent(applyEditingTemplateJava());

    this.getChildren().add(webview);
  }
}




import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;

import javax.swing.JFrame;

import fr.lri.swingstates.canvas.CExtensionalTag;
import fr.lri.swingstates.canvas.CRectangle;
import fr.lri.swingstates.canvas.CShape;
import fr.lri.swingstates.canvas.CStateMachine;
import fr.lri.swingstates.canvas.CText;
import fr.lri.swingstates.canvas.Canvas;
import fr.lri.swingstates.canvas.transitions.EnterOnTag;
import fr.lri.swingstates.canvas.transitions.LeaveOnTag;
import fr.lri.swingstates.canvas.transitions.PressOnTag;
import fr.lri.swingstates.canvas.transitions.ReleaseOnTag;


import fr.lri.swingstates.debug.StateMachineVisualization;
import fr.lri.swingstates.sm.State;
import fr.lri.swingstates.sm.Transition;
import fr.lri.swingstates.sm.transitions.Release;
import fr.lri.swingstates.sm.transitions.TimeOut;

/**
 * @author Nicolas Roussel (roussel@lri.fr)
 *
 */
public class SimpleButton {

	private CText label ;
	private CRectangle rectangle;
	final private CExtensionalTag selected;
	public CStateMachine sm;

	SimpleButton(final Canvas canvas, String text) {
		label = canvas.newText(0, 0, text, new Font("verdana", Font.PLAIN, 12)) ;
		rectangle = canvas.newRectangle(label.getReferenceX(), label.getReferenceY(), label.getWidth(), label.getHeight());
		rectangle.addChild(label);
		rectangle.setFilled(false);



		selected = new CExtensionalTag(canvas) {
			public void added(CShape s) { 

			}
			public void removed(CShape s) {

			}
		};
		//rectangle.addTag(selected);
		label.addTag(selected);


		sm = new CStateMachine() {
			
			CShape shape = getShape();
			
			
			Color initColor;
			
			
			public State idle = new State() {
				// This transition is triggered by any mouse press on a shape having a tag of class Selected

				Transition enterBox = new EnterOnTag(selected, ">> hover") {
					public void action() {
						System.out.println("EnterOnTag");
						getShape().setOutlined(true).setStroke(new BasicStroke(2));
					}
				};
				
			};
			public State hover = new State() {
				
				Transition leaveBox = new LeaveOnTag(selected, ">> idle") {
					public void action() {
						System.out.println("LeaveOnTag");
						getShape().setStroke(new BasicStroke(0));
					}
				};
				
				Transition pressOnShape = new PressOnTag(selected, BUTTON1, ">> timer") {
					public void action() {
						armTimer("selected", 500, false);
						System.out.println("pressOnTag");
						initColor = (Color)(getShape().getFillPaint());
						getShape().setFillPaint(Color.YELLOW);
						//getShape().setOutlinePaint(Color.YELLOW);
					}
				};
			};
			
			public State timer = new State() {
				Transition pressOnShape = new PressOnTag(selected, BUTTON1, ">> pressed") {
					public void action() {
						System.out.println("pressOnTag into Timer");
						//initColor = (Color)(getShape().getFillPaint());
						//getShape().setFillPaint(Color.YELLOW);
						//getShape().setOutlinePaint(Color.YELLOW);
					}
				};
				
				
				
				/*Release release = new Release(">> idle") {
					public void action() {
						System.out.println("releaseOnTag");
						shape.setFillPaint(Color.black);
						//getShape().removeTag(selected);
					}
				};*/
				
				Transition enterBox = new EnterOnTag(selected, ">> pressed") {
					public void action() {
						System.out.println("EnterOnTag");
						getShape().setOutlined(true).setStroke(new BasicStroke(2));
					}
				};
				
				Transition timeOut = new TimeOut(">> hover") {
					public void action(){
						System.out.println("time out");
						
						
					}
				};
			};
			
			public State pressed = new State() {
				Transition release = new ReleaseOnTag(selected, BUTTON1, ">> hover") {
					public void action() {
						System.out.println("releaseOnTag");
						getShape().setFillPaint(initColor);
						//getShape().removeTag(selected);
					}
				};
				
				Transition leaveBox = new LeaveOnTag(selected, ">> desactivated") {
					public void action() {
						System.out.println("LeaveOnTag");
						getShape().setStroke(new BasicStroke(0));
						shape = getShape();
					}
				};


			};

			public State desactivated = new State() {

				Release release = new Release(">> idle") {
					public void action() {
						System.out.println("releaseOnTag");
						shape.setFillPaint(Color.black);
						//getShape().removeTag(selected);
					}
				};
				
				Transition enterBox = new EnterOnTag(selected, ">> pressed") {
					public void action() {
						System.out.println("EnterOnTag");
						getShape().setOutlined(true).setStroke(new BasicStroke(2));
					}
				};
			};
		};

		sm.attachTo(canvas);





	}

	public void action() {
		System.out.println("ACTION!") ;
	}

	public CShape getShape() {
		return rectangle ;
	}

	static public void main(String[] args) {
		JFrame frame = new JFrame() ;
		Canvas canvas = new Canvas(400,400) ;
		frame.getContentPane().add(canvas) ;
		frame.pack() ;
		frame.setVisible(true) ;

		SimpleButton simple = new SimpleButton(canvas, "simple") ;
		simple.getShape().translateBy(100,100) ;
		
		
		
		JFrame viz = new JFrame();
		viz.getContentPane().add(new StateMachineVisualization(simple.sm));
		viz.pack();
		viz.setVisible(true);
	}

}

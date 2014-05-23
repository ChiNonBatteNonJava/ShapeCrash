package com.chinonbattenonjava.saproject;

import java.util.ArrayList;
import java.util.List;

import ClientSide.Client;
import android.view.MotionEvent;

import com.badlogic.gdx.math.Vector2;


interface ICommand {
	public void execute();
}


public class GameEventListener {
	CommandListDeclaration commands;
	public GameEventListener(CommandListDeclaration commands) {
		
		this.commands=commands;
	}

	public boolean onTouchEvent(final MotionEvent event) {
		int action = event.getAction();
		commands.randomTouch(event.getX(), event.getY());
		switch (action) {
		case MotionEvent.ACTION_DOWN: {// MotionEvent class field
			if (event.getX() < 400) {
				commands.leftTouch();
				return true;
			} else {
				commands.rightTouch();
				return true;
			}
		}
		case MotionEvent.ACTION_UP:
			commands.upTouch();
			return true;
		}
		return true;

	}

	public CommandListDeclaration getListDeclaration(){
		return commands;
	}
	
}

class CommandListDeclaration {
	class BoxCommand{
		public Vector2 p1;
		public Vector2 p2;
		public ICommand command;
		public BoxCommand(Vector2 p1,Vector2 p2,ICommand command){
			this.p1=p1;
			this.p2=p2;
			this.command=command;
		}
		public boolean touchIn(Vector2 pos){
			
			return (pos.x>=p1.x && pos.x<=p2.x && pos.y>=p1.y && pos.y<=p2.y);
		}
		
	}
	private List<ICommand>leftTouchCOmmand;
	private List<ICommand>rightTouchCOmmand;
	private List<ICommand>upTouchCOmmand;
	private List<BoxCommand>boxTouchCOmmand;
	
	public CommandListDeclaration(){
		leftTouchCOmmand=new ArrayList<ICommand>();
		rightTouchCOmmand=new ArrayList<ICommand>();
		upTouchCOmmand=new ArrayList<ICommand>();
		boxTouchCOmmand=new ArrayList<BoxCommand>();
		
	}
	
	public void addLeftTouchAction(ICommand c){
		leftTouchCOmmand.add(c);		
	}
	public void addRightTouchAction(ICommand c){
		rightTouchCOmmand.add(c);		
	}
	public void addUpTouchAction(ICommand c){
		upTouchCOmmand.add(c);		
	}
	public void addBoxTouchAction(ICommand c,Vector2 p1,Vector2 p2){
		boxTouchCOmmand.add(new BoxCommand(p1,p2,c));
		
	}
	public void leftTouch(){
		for(ICommand i :leftTouchCOmmand){
			i.execute();
		}
	}
	public void rightTouch(){
		for(ICommand i :rightTouchCOmmand){
			i.execute();
		}
	}
	public void upTouch(){
		for(ICommand i :upTouchCOmmand){
			i.execute();
		}	
	}
	public void randomTouch(float x,float y){
		for(BoxCommand i :boxTouchCOmmand){
			if(i.touchIn(new Vector2(x,y))){
				i.command.execute();
			}
		}
		
	}

}


class RetroCommand implements ICommand{
	Car c;
	public RetroCommand(Car c){
		this.c=c;
	}
	@Override
	public void execute() {
		c.getCar().setRetro();
		
	}
	
	
}

class ResetRetroCommand implements ICommand{
	Car c;
	public ResetRetroCommand(Car c){
		this.c=c;
	}
	@Override
	public void execute() {
		c.getCar().resetRetro();
		
	}
	
	
}

class TournLeft implements ICommand {
	Car myCar;
	public TournLeft(Car c) {
		myCar = c;
	}
	@Override
	public void execute() {
		myCar.getCar().LeftSteering();
		new Sender(myCar.getCar().getRetro(), -1).start();
	}

}

class TournRight implements ICommand {
	Car myCar;
	public TournRight(Car c) {
		myCar = c;

	}
	@Override
	public void execute() {
		myCar.getCar().RightSteering();
		new Sender(myCar.getCar().getRetro(), 1).start();
	}
}

class ResetSteering implements ICommand {
	Car myCar;
	public ResetSteering(Car c) {
		myCar = c;
	}
	@Override
	public void execute() {
		myCar.getCar().SetSteering(0);
		new Sender(myCar.getCar().getRetro(), 0).start();
	}
	
}




/**
 * 
 * Builder statico per creare azioni car
 */
class CarActionBuilder{
	static void Create(Car c,GameEventListener lin){
		lin.getListDeclaration().addLeftTouchAction(new TournRight(c));
		lin.getListDeclaration().addRightTouchAction(new TournLeft(c));
		lin.getListDeclaration().addUpTouchAction(new ResetRetroCommand(c));
		lin.getListDeclaration().addUpTouchAction(new ResetSteering(c));
		lin.getListDeclaration().addBoxTouchAction(new RetroCommand(c),new Vector2(GameResourceManager.getInstance().getScreenSize().x/2-50))
	}
	
}

class Sender extends Thread{
	
	int dir;
	int steering;
	
	public Sender(int d, int s){
		dir = d;
		steering = s;
	}
	
	public void run(){
		Client.getInstance().setSteering(dir, steering);
	}
	
}


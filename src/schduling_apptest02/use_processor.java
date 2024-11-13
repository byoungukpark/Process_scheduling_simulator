package schduling_apptest02;

import java.util.ArrayList;

class use_processor {
	ArrayList<core> all_processor = new ArrayList<>(); 
	ArrayList<Integer> co_index = new ArrayList<>(); 
	
	class core{
		ArrayList<Integer> time = new ArrayList<>(); //일처리 과정 가록
		char type; //코어 타입 구분
		float electric; //소모 전류
		boolean flag = false; //코어의 일여부
		int processes_index = 0; //사용중인 프로세스 확인
		int count_bt = 0; // 사용중인 프로세스 종료 조건
		int core_using_time = 0; //프로세스가 현재 코어 사용 시간 //rr
		
		core(char type){
			this.type = type;
		}
	}
	
	use_processor(int p,int e){
		if((p+e) <= 4 && (p+e) != 0) {
			for(int i = 0;i<p;i++) {
				core new_core = new core('P');
				all_processor.add(new_core);
			}
			for(int i = 0;i<e;i++) {
				core new_core = new core('E');
				all_processor.add(new_core);
			}
		}else {
			System.out.println("ERRRRRRRRRRRRRR");
			System.out.println(" >> 4 P_core set!! >> Play");
			for(int i = 0;i<4;i++) {
				core new_core = new core('P');
				all_processor.add(new_core);
			}
		}
	}
	
	public boolean is_canplay() {
		co_index.clear();
		for(int i = 0;i<all_processor.size();i++) {
			if(!all_processor.get(i).flag) {
				co_index.add(i);
			}
		}
		if(co_index.size() == 0)
			return false;
		else
			return true;
	}
	
	public void enter_co(int co_index,int ready_index,int past_bt, int using_time) {
		all_processor.get(co_index).flag = true;
		all_processor.get(co_index).processes_index = ready_index;
		
		all_processor.get(co_index).count_bt = past_bt; //rr
		all_processor.get(co_index).core_using_time = using_time;//rr
	}
	
	public void kepwork_co() {
		for(int i = 0;i<all_processor.size();i++) {
			if(all_processor.get(i).flag == false) {
				all_processor.get(i).time.add(0);
			}else {
				all_processor.get(i).time.add(all_processor.get(i).processes_index + 1);
				culculate_el(i);
				
				all_processor.get(i).core_using_time++; //rr_schduling
			}
		}
	}
	
	public void clear_co(int co_index) {
		all_processor.get(co_index).flag = false;
		all_processor.get(co_index).processes_index = 0;
		all_processor.get(co_index).count_bt = 0;
		
		all_processor.get(co_index).core_using_time = 0; //rr_schduling
	}
	
	void culculate_el(int co_index) {
		if(all_processor.get(co_index).type == 'P') {
			if(all_processor.get(co_index).time.size() == 1)
				all_processor.get(co_index).electric += 0.5;
			
			if(all_processor.get(co_index).time.size() >= 2 && all_processor.get(co_index).time.get(all_processor.get(co_index).time.size()-2) == 0)
				all_processor.get(co_index).electric += 0.5;
			
			all_processor.get(co_index).electric += 3;
			all_processor.get(co_index).count_bt += 2;
			
		}else if(all_processor.get(co_index).type == 'E'){
			if(all_processor.get(co_index).time.size() == 1)
				all_processor.get(co_index).electric += 0.1;
			
			if(all_processor.get(co_index).time.size() >= 2 && all_processor.get(co_index).time.get(all_processor.get(co_index).time.size()-2) == 0)
				all_processor.get(co_index).electric += 0.1;
			
			all_processor.get(co_index).electric += 1;
			all_processor.get(co_index).count_bt++;
		}
	}
	
	public boolean is_canplay_e() { // 추가됨!! 사용가능한 e 코어 프로세서 확인
		co_index.clear();
		for(int i = 0;i<all_processor.size();i++) {
			if(!all_processor.get(i).flag && all_processor.get(i).type == 'E') {
				co_index.add(i);
			}
		}
		if(co_index.size() == 0)
			return false;
		else
			return true;
	}
	
	public boolean is_canplay_p() { // 추가됨!! 사용가능한 p 코어 프로세서 확인
		co_index.clear();
		for(int i = 0;i<all_processor.size();i++) {
			if(!all_processor.get(i).flag && all_processor.get(i).type == 'P') {
				co_index.add(i);
			}
		}
		if(co_index.size() == 0)
			return false;
		else
			return true;
	}
}

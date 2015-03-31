package com.dimuthuupeksha.svn;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

public class AccessManager {
	private String fileURL = "/home/wso2123/svn_auth.txt";

	public String[] getProjectList() {
		String projects[] = { "project" };
		List<String> proList = new ArrayList<String>();
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(new File(fileURL)));

			String line;
			boolean groupsAvl = false;
			String prefix = "[projects:";
			while ((line = br.readLine()) != null) {
				if (line.trim().length() > prefix.length()) {
					if (line.trim().substring(0, prefix.length())
							.equals(prefix)) {
						String firstPart = line.substring(prefix.length(),
								line.length());
						String secondPart = firstPart.substring(0,
								firstPart.length() - 1);
						proList.add(secondPart);
					}
				}
			}
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Project List :");
		for (String string : proList) {

			System.out.println(string);
		}
		projects = proList.toArray(projects);
		return projects;
	}

	public String[] getGroupList() {
		String groups[] = { "group1" };
		List<String> grpList = new ArrayList<String>();
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(new File(fileURL)));

			String line;
			boolean groupsAvl = false;
			while ((line = br.readLine()) != null) {
				if (line.length() > 0) {
					if (line.charAt(0) == '[') {
						groupsAvl = false;
					}
				}
				if (groupsAvl) {
					if (line.length() > 0) {
						String data[] = line.split("=");
						if (data != null && data.length == 2) {
							grpList.add(data[0].trim());
						}
					}
				}

				if (line.trim().equals("[groups]")) {
					groupsAvl = true;
				}
			}
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Group List :");
		for (String string : grpList) {

			System.out.println(string);
		}
		groups = grpList.toArray(groups);
		return groups;
	}

	public String[] getUserGroupList(String mail) {
		String groups[] = { "group1" };
		List<String> grpList = new ArrayList<String>();
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(new File(fileURL)));

			String line;
			boolean groupsAvl = false;
			while ((line = br.readLine()) != null) {
				if (line.length() > 0) {
					if (line.charAt(0) == '[') {
						groupsAvl = false;
					}
				}
				if (groupsAvl) {
					if (line.length() > 0) {
						String data[] = line.split("=");
						if (data != null && data.length == 2) {
							boolean available = false;
							String users[] = data[1].split(",");
							if (users != null) {
								for (int i = 0; i < users.length; i++) {
									
									if (users[i].trim().equals(mail.trim())) {
										available = true;
										break;
									}
								}
							}
							if (available) {
								grpList.add(data[0].trim());
							}
						}
					}
				}

				if (line.trim().equals("[groups]")) {
					groupsAvl = true;
				}
			}
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		groups = grpList.toArray(groups);
		return groups;
	}

	public String[] getUserList(String groupId) {
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(new File(fileURL)));

			String line;
			boolean groupsAvl = false;
			while ((line = br.readLine()) != null) {
				if (line.length() > 0) {
					if (line.charAt(0) == '[') {
						groupsAvl = false;
					}
				}
				if (groupsAvl) {
					if (line.length() > 0) {
						String data[] = line.split("=");
						if (data != null && data.length == 2) {
							if (data[0].trim().equals(groupId)) {
								br.close();
								return data[1].trim().split(",");
							}
						}
					}
				}

				if (line.trim().equals("[groups]")) {
					groupsAvl = true;
				}
			}
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Group List :");

		return null;
	}

	public String addUserToGroup(String groupId, String mail) {
		List<String> fileLines = new ArrayList<>();
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(new File(fileURL)));

			String line;
			boolean groupsAvl = false;
			while ((line = br.readLine()) != null) {

				if (line.length() > 0) {
					if (line.charAt(0) == '[') {
						groupsAvl = false;
					}
				}
				if (groupsAvl) {
					if (line.length() > 0) {
						String data[] = line.split("=");
						if (data != null && data.length >= 1) {
							if (data[0].trim().equals(groupId)) {
								if (data.length == 2
										&& data[1].trim().length() > 0) {
									line = line + "," + mail;
								} else {
									line = line + mail;
								}
							}
						}
					}
				}

				if (line.trim().equals("[groups]")) {
					groupsAvl = true;
				}
				fileLines.add(line);
			}

			br.close();
			BufferedWriter bw = new BufferedWriter(new FileWriter(fileURL,
					false));
			for (String string : fileLines) {
				System.out.println(string);
				bw.write(string);
				bw.newLine();
			}
			bw.flush();
			bw.close();
			return "Success";
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "Fail";
	}

	public String createGroup(String groupId) {
		List<String> fileLines = new ArrayList<>();
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(new File(fileURL)));

			String line;
			boolean groupsAvl = false;
			while ((line = br.readLine()) != null) {

				if (line.length() > 0) {
					if (line.charAt(0) == '[') {
						if (groupsAvl) {
							fileLines.add(groupId + " = \n");
						}
						groupsAvl = false;
					}
				}

				if (line.trim().equals("[groups]")) {
					groupsAvl = true;
				}
				fileLines.add(line);
			}

			br.close();
			BufferedWriter bw = new BufferedWriter(new FileWriter(fileURL,
					false));
			for (String string : fileLines) {
				System.out.println(string);
				bw.write(string);
				bw.newLine();
			}
			bw.flush();
			bw.close();
			return "Success";
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "Fail";
	}

	public String addProject(String projectPath) {
		List<String> fileLines = new ArrayList<>();
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(new File(fileURL)));

			String line;
			boolean groupsAvl = false;
			while ((line = br.readLine()) != null) {

				if (line.length() > 0) {
					if (line.charAt(0) == '[') {
						groupsAvl = false;
					}
				}

				if (line.trim().equals("[groups]")) {
					groupsAvl = true;
				}
				fileLines.add(line);
			}
			fileLines.add("\n");
			fileLines.add("[projects:" + projectPath + "]");
			br.close();
			BufferedWriter bw = new BufferedWriter(new FileWriter(fileURL,
					false));
			for (String string : fileLines) {
				System.out.println(string);
				bw.write(string);
				bw.newLine();
			}
			bw.flush();
			bw.close();
			return "Success";
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "Fail";
	}

	public String addGroupToProject(String projectPath, String groupId,
			String priviledge) {
		List<String> fileLines = new ArrayList<>();
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(new File(fileURL)));

			String line;
			while ((line = br.readLine()) != null) {

				fileLines.add(line);
				if (line.trim().equals("[projects:" + projectPath.trim() + "]")) {
					fileLines.add("@" + groupId + " = " + priviledge);
				}
			}
			br.close();
			BufferedWriter bw = new BufferedWriter(new FileWriter(fileURL,
					false));
			for (String string : fileLines) {
				System.out.println(string);
				bw.write(string);
				bw.newLine();
			}
			bw.flush();
			bw.close();
			return "Success";
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "Fail";

	}

	public String removeGroupFromProject(String projectPath, String groupId) {
		List<String> fileLines = new ArrayList<>();
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(new File(fileURL)));
			boolean projectFound = false;
			boolean groupFound = false;
			String line;
			while ((line = br.readLine()) != null) {
				if (projectFound) {
					if (line.trim().length() > ("@" + groupId).length()
							&& line.trim()
									.substring(0, ("@" + groupId).length())
									.equals("@" + groupId)) {
						groupFound = true;
					}
				}
				if (line.length() > 0) {
					if (line.charAt(0) == '[') {
						projectFound = false;
					}
				}

				if (groupFound) {
					groupFound = false;
				} else {
					fileLines.add(line);
				}
				if (line.trim().equals("[projects:" + projectPath.trim() + "]")) {
					projectFound = true;
				}
			}
			br.close();
			BufferedWriter bw = new BufferedWriter(new FileWriter(fileURL,
					false));
			for (String string : fileLines) {
				System.out.println(string);
				bw.write(string);
				bw.newLine();
			}
			bw.flush();
			bw.close();
			return "Success";
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "Fail";

	}

	public String removeUserFromGroup(String groupId, String mail) {
		List<String> fileLines = new ArrayList<>();
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(new File(fileURL)));

			String line;
			boolean groupsAvl = false;
			while ((line = br.readLine()) != null) {

				if (line.length() > 0) {
					if (line.charAt(0) == '[') {
						groupsAvl = false;
					}
				}

				if (groupsAvl) {
					if (line.length() > 0) {
						String data[] = line.split("=");
						if (data != null && data.length >= 1) {
							if (data[0].trim().equals(groupId)) {
								if (data.length == 2
										&& data[1].trim().length() > 0) {
									String users[] = data[1].trim().split(",");
									String newLine = groupId + " = ";
									boolean start = true;
									for (int i = 0; i < users.length; i++) {
										if (users[i].trim().equals(mail)) {
											continue;
										}
										if (start) {
											newLine += users[i];
											start = false;
										} else {
											newLine += "," + users[i];
										}
									}
									line = newLine;
								}
							}
						}
					}
				}

				if (line.trim().equals("[groups]")) {
					groupsAvl = true;
				}
				fileLines.add(line);
			}
			br.close();
			BufferedWriter bw = new BufferedWriter(new FileWriter(fileURL,
					false));
			for (String string : fileLines) {
				System.out.println(string);
				bw.write(string);
				bw.newLine();
			}
			bw.flush();
			bw.close();
			return "Success";
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "Fail";
	}

	public String deleteGroup(String groupId) {
		List<String> fileLines = new ArrayList<>();
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(new File(fileURL)));

			String line;
			boolean groupsAvl = false;
			boolean groupDetected = false;
			while ((line = br.readLine()) != null) {

				if (line.length() > 0) {
					if (line.charAt(0) == '[') {
						groupsAvl = false;
					}
				}

				if (groupsAvl) {
					if (line.length() > 0) {
						String data[] = line.split("=");
						if (data != null && data.length >= 1) {
							if (data[0].trim().equals(groupId)) {
								groupDetected = true;
							}
						}
					}
				}

				if (line.trim().equals("[groups]")) {
					groupsAvl = true;
				}

				if (groupDetected) {
					groupDetected = false;
				} else {
					fileLines.add(line);
				}
			}
			br.close();
			BufferedWriter bw = new BufferedWriter(new FileWriter(fileURL,
					false));
			for (String string : fileLines) {
				System.out.println(string);
				bw.write(string);
				bw.newLine();
			}
			bw.flush();
			bw.close();
			return "Success";
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "Fail";
	}

	public String deleteProject(String projectPath) {
		List<String> fileLines = new ArrayList<>();
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(new File(fileURL)));
			boolean projectFound = false;
			boolean groupFound = false;
			String line;
			while ((line = br.readLine()) != null) {

				if (line.length() > 0) {
					if (line.charAt(0) == '[') {
						projectFound = false;
					}
				}

				if (line.trim().equals("[projects:" + projectPath.trim() + "]")) {
					projectFound = true;
				}
				if (!projectFound) {
					fileLines.add(line);
				}
			}
			br.close();
			BufferedWriter bw = new BufferedWriter(new FileWriter(fileURL,
					false));
			for (String string : fileLines) {
				System.out.println(string);
				bw.write(string);
				bw.newLine();
			}
			bw.flush();
			bw.close();
			return "Success";
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "Fail";
	}

	public String[] getGroupsFromProject(String projectPath) {
		List<String> lines = new ArrayList<>();
		String groups[] = {};
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(new File(fileURL)));
			boolean projectFound = false;
			String line;
			while ((line = br.readLine()) != null) {

				if (line.length() > 0) {
					if (line.charAt(0) == '[') {
						projectFound = false;
					}
				}
				if (projectFound) {
					if (line.trim().length() > 0
							&& line.trim().charAt(0) == '@') {
						lines.add(line.trim()
								.substring(1, line.trim().length()));
					}
				}

				if (line.trim().equals("[projects:" + projectPath.trim() + "]")) {
					projectFound = true;
				}

			}
			groups = lines.toArray(groups);
			br.close();

			return groups;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	public String removeUser(String mail) {
		List<String> fileLines = new ArrayList<>();
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(new File(fileURL)));

			String line;
			boolean groupsAvl = false;
			while ((line = br.readLine()) != null) {

				if (line.length() > 0) {
					if (line.charAt(0) == '[') {
						groupsAvl = false;
					}
				}
				if (groupsAvl) {
					if (line.length() > 0) {
						String data[] = line.split("=");
						
						if (data != null && data.length >= 1) {
							line = data[0]+"=";
							String users[] = data[1].split(",");
							if(users!=null){
								boolean start=true;
								for(int i=0;i<users.length;i++){
									if(users[i].trim().equals(mail)){
										continue;
									}
									if(start){
										line=line+users[i];
										start=false;
									}else{
										line=line+","+users[i];
									}
								}
							}
						}
					}
				}

				if (line.trim().equals("[groups]")) {
					groupsAvl = true;
				}
				fileLines.add(line);
			}

			br.close();
			BufferedWriter bw = new BufferedWriter(new FileWriter(fileURL,
					false));
			for (String string : fileLines) {
				System.out.println(string);
				bw.write(string);
				bw.newLine();
			}
			bw.flush();
			bw.close();
			return "Success";
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "Fail";
	}
}
{
 pkgs ?  import <nixpkgs> {} 
}:
pkgs.mkShell {
    packages = with pkgs; [
    openjdk25 
    maven
    ];
  }

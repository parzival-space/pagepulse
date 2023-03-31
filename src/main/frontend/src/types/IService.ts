import { Status } from "./Status";

export interface IService {
  id: number,
  name: string,
  group: string,
  endpoint: string | null
}
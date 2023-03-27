import { Status } from "./Status";

export interface IHistoryEntry {
  timestamp: String,
  status: Status,
  error: string | null,
  possibleCause: string | null
}